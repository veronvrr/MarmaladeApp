// grammar-check-hybrid.js
document.addEventListener('DOMContentLoaded', () => {
  const csrfMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
  const CSRF_TOKEN = csrfMeta ? csrfMeta.getAttribute('content') : null;
  const CSRF_HEADER = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN';

  const form = document.getElementById('grammarForm');
  const checkAllBtn = document.getElementById('checkAll');
  const globalResult = document.getElementById('globalResult');
  const cards = Array.from(document.querySelectorAll('.question-card'));

  if (cards.length === 0) {
    console.log('No question cards found');
    return;
  }

  // ===== Локальная проверка =====
  function normalize(s) {
    if (!s) return "";
    return String(s).trim().toLowerCase();
  }

  function isCorrect(answerValue, correctString) {
    if (!correctString) return false;
    const normalizedUser = normalize(answerValue);
    const alternatives = correctString.split('|').map(a => a.trim().toLowerCase());
    return alternatives.includes(normalizedUser);
  }

  function checkSingle(card) {
    const qid = card.dataset.qid;
    const hidden = document.getElementById('ans-' + qid);
    const correct = hidden ? hidden.value : "";
    const resultEl = card.querySelector('.q-result');

    const inputs = card.querySelectorAll(`[name="answer_${qid}"]`);
    let userAnswer = "";

    if (inputs.length > 0) {
      if (inputs[0].type === 'radio') {
        const selected = Array.from(inputs).find(i => i.checked);
        userAnswer = selected ? selected.value : "";
      } else {
        userAnswer = inputs[0].value || "";
      }
    }

    const ok = isCorrect(userAnswer, correct);
    resultEl.innerHTML = ok
      ? '<span style="color:#2b8a4b">✓</span>'
      : `<span style="color:#d04545">✗</span> <small>Правильно: ${correct.split('|')[0] || ''}</small>`;

    card.classList.toggle('correct', ok);
    card.classList.toggle('wrong', !ok);
    return { questionId: qid, answer: userAnswer, correct: ok };
  }

  function checkAllLocal() {
    let correctCount = 0;
    const results = cards.map(c => {
      const r = checkSingle(c);
      if (r.correct) correctCount++;
      return r;
    });
    globalResult.innerHTML = `<strong>${correctCount} / ${cards.length}</strong>`;
    globalResult.style.color = (correctCount === cards.length) ? '#2b8a4b' : '#d04545';
    return results;
  }

  // ===== Серверная проверка =====
  async function checkOnServer(payload) {
    try {
      const headers = { 'Content-Type': 'application/json' };
      if (CSRF_TOKEN) headers[CSRF_HEADER] = CSRF_TOKEN;

      const res = await fetch('/api/modules/grammar/check', {
        method: 'POST',
        headers,
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        console.error('Server error:', res.status);
        return;
      }

      const result = await res.json();
      console.log('Server result:', result);
      // можно дополнительно обновить статистику, прогресс‑бар и т.д.
    } catch (err) {
      console.error('Network error', err);
    }
  }

  // ===== Привязка событий =====
  checkAllBtn?.addEventListener('click', () => {
    const payload = checkAllLocal(); // локальная проверка
    checkOnServer(payload);          // отправка на сервер
  });

  form?.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      const card = e.target.closest('.question-card');
      if (card) {
        e.preventDefault();
        checkSingle(card); // локальная проверка одного вопроса
      }
    }
  });

});
