document.addEventListener('DOMContentLoaded', () => {
  const checkAllBtn = document.getElementById('checkAll');

  function normalize(s) {
    return (s || '').toString().trim().toLowerCase();
  }

  function getCorrect(qid) {
    const hidden = document.getElementById('ans-' + qid);
    return hidden ? hidden.value : '';
  }

  function checkCard(card) {
    const qid = card.getAttribute('data-task-id');
    const name = 'answer_' + qid;
    const inputs = card.querySelectorAll('[name="' + name + '"]');
    let userAnswer = '';

    if (inputs.length === 0) return false;

    const first = inputs[0];
    if (first.type === 'radio') {
      const sel = Array.from(inputs).find(i => i.checked);
      userAnswer = sel ? sel.value : '';
    } else {
      userAnswer = first.value || '';
    }

    const correct = getCorrect(qid);
    const alts = correct.split('|').map(a => a.trim().toLowerCase());
    const ok = alts.includes(normalize(userAnswer));

    const resultEl = card.querySelector('.q-result');
    if (ok) {
      card.classList.add('correct'); card.classList.remove('wrong');
      resultEl.innerHTML = '<span style="color:#2b8a4b">✓</span>';
    } else {
      card.classList.add('wrong'); card.classList.remove('correct');
      const firstCorrect = alts[0] || '';
      resultEl.innerHTML = `<span style="color:#d04545">✗</span> <small>Правильно: ${firstCorrect}</small>`;
    }
    return ok;
  }

  function checkAll() {
    const cards = Array.from(document.querySelectorAll('.question-card'));
    let total = cards.length, correct = 0;
    cards.forEach(c => { if (checkCard(c)) correct++; });
    const global = document.getElementById('globalResult');
    if (global) global.innerHTML = `<strong>${correct} / ${total}</strong>`;
  }

  checkAllBtn?.addEventListener('click', checkAll);
});
