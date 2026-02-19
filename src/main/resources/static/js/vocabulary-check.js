// static/js/vocabulary-check.js
document.addEventListener('DOMContentLoaded', () => {
  // форма должна иметь id="vocabularyForm" и data-video-id="<id>"
  const form = document.getElementById('vocabularyForm');
  const checkBtn = document.getElementById('vocabCheckBtn');
  const globalResult = document.getElementById('globalResult');

  // CSRF meta (Thymeleaf pages usually set these)
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-CSRF-TOKEN';

  function gatherAnswers() {
    const answers = [];
    const cards = Array.from(document.querySelectorAll('.question-card'));
    cards.forEach(card => {
      const qid = card.getAttribute('data-task-id') || card.dataset.taskId;
      if (!qid) return;
      const name = 'answer_' + qid;
      const inputs = Array.from(card.querySelectorAll(`[name="${name}"]`));
      let value = '';
      if (inputs.length === 0) {
        // nothing found
        value = '';
      } else {
        const first = inputs[0];
        if (first.type === 'radio') {
          const sel = inputs.find(i => i.checked);
          value = sel ? sel.value : '';
        } else {
          value = first.value || '';
        }
      }
      answers.push({ questionId: String(qid), answer: value });
    });
    return answers;
  }

  function applyResult(perQuestion) {
    // perQuestion: { "123": true, "124": false, ... }
    Object.keys(perQuestion).forEach(qid => {
      const ok = perQuestion[qid];
      const card = document.querySelector(`.question-card[data-task-id="${qid}"]`);
      if (!card) return;
      const resultEl = card.querySelector('.q-result');
      if (ok) {
        card.classList.add('correct'); card.classList.remove('wrong');
        if (resultEl) resultEl.innerHTML = '<span style="color:#2b8a4b">✓</span>';
      } else {
        card.classList.add('wrong'); card.classList.remove('correct');
        // show correct answer from hidden input if present
        const hidden = document.getElementById('ans-' + qid);
        const expected = hidden ? (hidden.value || '') : '';
        const firstExpected = expected.split('|')[0] || '';
        if (resultEl) resultEl.innerHTML = `<span style="color:#d04545">✗</span> <small>Правильно: ${firstExpected}</small>`;
      }
    });
  }

  async function checkServer(videoId, answers) {
    try {
      const payload = { videoId: videoId, answers: answers };
      const headers = { 'Content-Type': 'application/json' };
      if (csrfToken) headers[csrfHeader] = csrfToken;
      const resp = await fetch('/api/modules/vocabulary/check', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(payload)
      });
      if (!resp.ok) throw new Error('Server returned ' + resp.status);
      const json = await resp.json();
      return json; // expected CheckResult { correct, total, perQuestion }
    } catch (err) {
      console.warn('Server check failed:', err);
      return null;
    }
  }

  function localFallbackCheck() {
    // локальная проверка по скрытым инпутам ans-<id>
    const per = {};
    let total = 0, correct = 0;
    const cards = Array.from(document.querySelectorAll('.question-card'));
    cards.forEach(card => {
      const qid = card.getAttribute('data-task-id') || card.dataset.taskId;
      if (!qid) return;
      const name = 'answer_' + qid;
      const inputs = Array.from(card.querySelectorAll(`[name="${name}"]`));
      let userValue = '';
      if (inputs.length === 0) { userValue = ''; }
      else {
        const f = inputs[0];
        if (f.type === 'radio') {
          const sel = inputs.find(i => i.checked);
          userValue = sel ? sel.value : '';
        } else {
          userValue = f.value || '';
        }
      }
      const hidden = document.getElementById('ans-' + qid);
      const expected = hidden ? hidden.value : '';
      const alt = expected ? expected.split('|').map(x => x.trim().toLowerCase()) : [];
      const normUser = (userValue || '').toString().trim().toLowerCase();
      let ok = alt.includes(normUser);
      per[qid] = ok;
      total++;
      if (ok) correct++;
    });
    return { correct, total, perQuestion: per };
  }

  checkBtn?.addEventListener('click', async (e) => {
    e.preventDefault();
    if (!form) return;
    const videoId = form.dataset.videoId ? Number(form.dataset.videoId) : null;
    const answers = gatherAnswers();

    checkBtn.disabled = true;
    checkBtn.textContent = 'Проверяем...';

    const serverResult = await checkServer(videoId, answers);
    if (serverResult) {
      applyResult(serverResult.perQuestion || {});
      if (globalResult) {
        globalResult.innerHTML = `<strong>${serverResult.correct} / ${serverResult.total}</strong>`;
        globalResult.style.color = serverResult.correct === serverResult.total ? '#2b8a4b' : '#d04545';
      }
    } else {
      // fallback на клиентскую проверку
      const local = localFallbackCheck();
      applyResult(local.perQuestion || {});
      if (globalResult) {
        globalResult.innerHTML = `<strong>${local.correct} / ${local.total}</strong>`;
        globalResult.style.color = local.correct === local.total ? '#2b8a4b' : '#d04545';
      }
    }

    checkBtn.disabled = false;
    checkBtn.textContent = 'Проверить';
  });
});
