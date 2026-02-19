// listening-check.js
(function(){
  function normalize(s){ return (s||'').toString().trim().toLowerCase(); }

  function checkClientAndShow() {
    const cards = Array.from(document.querySelectorAll('.question-card'));
    let total = cards.length, correct = 0;
    cards.forEach(card => {
      const qid = card.getAttribute('data-task-id');
      const hidden = document.getElementById('ans-' + qid);
      const expected = hidden ? hidden.value : "";
      const inputs = card.querySelectorAll('[name="answer_' + qid + '"]');
      let userAnswer = "";
      if (inputs.length === 0) {
        userAnswer = "";
      } else {
        const first = inputs[0];
        if (first.type === 'radio') {
          const sel = Array.from(inputs).find(i => i.checked);
          userAnswer = sel ? sel.value : "";
        } else {
          userAnswer = first.value || "";
        }
      }
      let ok = false;
      if (expected && expected.length) {
        ok = expected.split('|').map(a=>a.trim().toLowerCase()).includes(normalize(userAnswer));
      }
      const resultEl = card.querySelector('.q-result');
      if (ok) {
        card.classList.add('correct'); card.classList.remove('wrong');
        if(resultEl) resultEl.innerHTML = '<span style="color:#2b8a4b">✓</span>';
        correct++;
      } else {
        card.classList.add('wrong'); card.classList.remove('correct');
        if(resultEl) {
          const exp = (expected || '').split('|')[0] || '';
          resultEl.innerHTML = `<span style="color:#d04545">✗</span> <small>Правильно: ${exp}</small>`;
        }
      }
    });
    const global = document.getElementById('globalResult');
    if (global) {
      global.innerHTML = `<strong>${correct} / ${total}</strong>`;
      global.style.color = (correct === total) ? '#2b8a4b' : '#d04545';
    }
  }

  // — если хочешь, можно отправлять на сервер (POST /api/modules/listening/check)
  async function checkServer() {
    const form = document.getElementById('listeningForm');
    if (!form) return checkClientAndShow();
    const audioId = form.getAttribute('data-audio-id');
    const answers = Array.from(document.querySelectorAll('.question-card')).map(card => {
      const qid = card.getAttribute('data-task-id');
      const inputs = card.querySelectorAll('[name="answer_' + qid + '"]');
      let value = "";
      if (inputs.length > 0) {
        const first = inputs[0];
        if (first.type === 'radio') {
          const sel = Array.from(inputs).find(i => i.checked);
          value = sel ? sel.value : "";
        } else {
          value = first.value || "";
        }
      }
      return { questionId: qid, answer: value };
    });

    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    const token = tokenMeta ? tokenMeta.getAttribute('content') : null;
    const headerName = headerMeta ? headerMeta.getAttribute('content') : 'X-CSRF-TOKEN';

    const checkBtn = document.getElementById('checkBtn');
    if (checkBtn) { checkBtn.disabled = true; checkBtn.textContent = 'Проверяю...'; }

    try {
      const resp = await fetch('/api/modules/listening/check', {
        method: 'POST',
        credentials: 'same-origin',
        headers: {
          'Content-Type': 'application/json',
          ...(token ? { [headerName]: token } : {})
        },
        body: JSON.stringify({ audioId: Number(audioId), answers })
      });
      if (!resp.ok) throw new Error('HTTP ' + resp.status);
      const json = await resp.json();
      // json: { correct, total, perQuestion: { "<taskId>": true/false, ... } }
      Object.entries(json.perQuestion || {}).forEach(([qid, ok]) => {
        const card = document.querySelector(`.question-card[data-task-id="${qid}"]`);
        if (!card) return;
        const resultEl = card.querySelector('.q-result');
        if (ok) {
          card.classList.add('correct'); card.classList.remove('wrong');
          if (resultEl) resultEl.innerHTML = '<span style="color:#2b8a4b">✓</span>';
        } else {
          card.classList.add('wrong'); card.classList.remove('correct');
          if (resultEl) {
            const hidden = document.getElementById('ans-' + qid);
            const correctOne = hidden ? (hidden.value.split('|')[0] || '') : '';
            resultEl.innerHTML = `<span style="color:#d04545">✗</span> <small>Правильно: ${correctOne}</small>`;
          }
        }
      });
      const global = document.getElementById('globalResult');
      if (global) {
        global.innerHTML = `<strong>${json.correct} / ${json.total}</strong>`;
        global.style.color = (json.correct === json.total) ? '#2b8a4b' : '#d04545';
      }
    } catch (err) {
      console.error(err);
      // fallback — клиентская проверка
      checkClientAndShow();
    } finally {
      if (checkBtn) { checkBtn.disabled = false; checkBtn.textContent = 'Проверить'; }
    }
  }

  // экспортируем публичные функции, loader вызовет initListeningChecks() и runListeningCheck()
  function initListeningChecks() {
    const btn = document.getElementById('checkBtn');
    if (btn) {
      btn.removeEventListener('click', runListeningCheck);
      btn.addEventListener('click', runListeningCheck);
    }
  }
  function runListeningCheck(){
    // сначала пытаемся серверную проверку (если endpoint отсутствует — fallback на клиент)
    checkServer();
  }

  window.initListeningChecks = initListeningChecks;
  window.runListeningCheck = runListeningCheck;

})();
