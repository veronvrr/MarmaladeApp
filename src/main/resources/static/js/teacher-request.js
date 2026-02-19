// static/js/teacher-request.js
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('teacherRequestForm');
  const ajaxBtn = document.getElementById('ajaxSubmitBtn');
  const resultEl = document.getElementById('reqResult');

  if (!form || !ajaxBtn || !resultEl) return;

  // CSRF (Thymeleaf вставляет meta _csrf/_csrf_header у тебя в шаблоне)
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-CSRF-TOKEN';

  // helper: вывести сообщение
  function show(msg, isError = false) {
    resultEl.innerHTML = '';
    const s = document.createElement('div');
    s.textContent = msg;
    s.style.color = isError ? '#d04545' : '#2b8a4b';
    resultEl.appendChild(s);
  }

  ajaxBtn.addEventListener('click', async (e) => {
    e.preventDefault();
    const fd = new FormData(form);
    const studentName = (fd.get('studentName') || '').toString().trim();

    if (!studentName) {
      show('Введите имя', true);
      return;
    }

    // блокируем UI
    ajaxBtn.disabled = true;
    const prevText = ajaxBtn.textContent;
    ajaxBtn.textContent = 'Отправка...';

    try {
      const headers = {};
      if (csrfToken) headers[csrfHeader] = csrfToken;

      const resp = await fetch(form.action, {
        method: 'POST',
        body: fd,
        headers: headers,
        credentials: 'same-origin'
      });

      if (resp.status === 401) {
        window.location.href = '/login';
        return;
      }

      if (resp.ok) {
        show('Заявка отправлена. Проверьте "Мои заявки" в профиле.');
        ajaxBtn.textContent = 'Отправлено';
      } else {
        const txt = await resp.text();
        console.warn('Request failed', resp.status, txt);
        show('Ошибка отправки (' + resp.status + ')', true);
        ajaxBtn.textContent = prevText;
        ajaxBtn.disabled = false;
      }
    } catch (err) {
      console.error('Ajax error', err);
      show('Ошибка сети — см. консоль', true);
      ajaxBtn.textContent = prevText;
      ajaxBtn.disabled = false;
    }
  });
});
