// простой toggle для глазика
document.addEventListener('DOMContentLoaded', function () {
  const btn = document.querySelector('.pw-toggle');
  const input = document.querySelector('.password-field input');

  if (!btn || !input) return;

  btn.addEventListener('click', function (e) {
    e.preventDefault();
    if (input.type === 'password') {
      input.type = 'text';
      btn.textContent = '🙈';
    } else {
      input.type = 'password';
      btn.textContent = '👀';
    }
  });

  // подстраховка: если у поля уже есть текст при загрузке — добавляем класс filled (для floating label)
  const changeFilled = () => {
    if (input.value && input.value.trim().length > 0) input.classList.add('filled');
    else input.classList.remove('filled');
  };
  changeFilled();
  input.addEventListener('input', changeFilled);
});
