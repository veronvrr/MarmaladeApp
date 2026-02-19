document.addEventListener('DOMContentLoaded', () => {
  const pwToggle = document.querySelector('.pw-toggle');
  const pwInput = document.getElementById('newPassword');
  const pwBar = document.querySelector('.pw-bar');
  const saveBtn = document.getElementById('saveBtn');
  const form = document.getElementById('profileForm');

  // показать/скрыть пароль
  if (pwToggle && pwInput) {
    pwToggle.addEventListener('click', (e) => {
      e.preventDefault();
      pwInput.type = pwInput.type === 'password' ? 'text' : 'password';
      pwToggle.textContent = pwInput.type === 'password' ? '🙈' : '👀';
    });
  }

  // простой индикатор силы пароля
  function passwordStrength(pwd) {
    let score = 0;
    if (!pwd) return 0;
    if (pwd.length >= 6) score++;
    if (/[0-9]/.test(pwd)) score++;
    if (/[A-Z]/.test(pwd) && /[a-z]/.test(pwd)) score++;
    if (/[^A-Za-z0-9]/.test(pwd)) score++;
    return Math.min(score, 4);
  }
  function updatePwBar() {
    if (!pwBar || !pwInput) return;
    const s = passwordStrength(pwInput.value);
    const pct = (s/4)*100;
    pwBar.style.width = pct + '%';
    if (s <= 1) pwBar.style.background = '#e74c3c';
    else if (s === 2) pwBar.style.background = '#f1c40f';
    else pwBar.style.background = '#8AA878';
  }
  if (pwInput) {
    pwInput.addEventListener('input', updatePwBar);
    updatePwBar();
  }

  // при отправке — включить спиннер и заблокировать кнопку
  if (form && saveBtn) {
    form.addEventListener('submit', (e) => {
      // простая клиентская валидация: если пароль есть и слишком короткий
      if (pwInput && pwInput.value && pwInput.value.length < 6) {
        e.preventDefault();
        alert('Пароль должен быть не менее 6 символов');
        pwInput.focus();
        return;
      }
      saveBtn.classList.add('loading');
      saveBtn.disabled = true;
      // allow form to submit normally
    });
  }
});
