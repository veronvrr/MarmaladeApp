// register.js — безопасный, надёжный обработчик для страницы регистрации
document.addEventListener('DOMContentLoaded', () => {
  console.log('[register.js] loaded');

  // === floating labels fallback (если используешь) ===
  document.querySelectorAll('.field.floating input').forEach(input => {
    const toggle = () => {
      if (input.value && input.value.trim().length > 0) input.classList.add('filled');
      else input.classList.remove('filled');
    };
    // небольшая задержка для автозаполнения браузера
    setTimeout(toggle, 80);
    input.addEventListener('input', toggle);
    input.addEventListener('change', toggle);
    input.addEventListener('blur', toggle);
  });

  // === Показ/скрытие пароля (pw-toggle) ===
  const pwContainers = document.querySelectorAll('.password-field');
  pwContainers.forEach(container => {
    const input = container.querySelector('input[type="password"], input[type="text"], input');
    let btn = container.querySelector('.pw-toggle');

    // если кнопки нет — создаём её и добавляем в контейнер
    if (!btn) {
      btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'pw-toggle';
      btn.setAttribute('aria-label', 'Показать пароль');
      btn.textContent = '🙈';
      // позиционируем (в случае, если CSS ожидает позицию absolute)
      btn.style.position = 'absolute';
      btn.style.right = '14px';
      btn.style.top = '50%';
      btn.style.transform = 'translateY(-50%)';
      btn.style.background = 'transparent';
      btn.style.border = 'none';
      btn.style.cursor = 'pointer';
      container.style.position = container.style.position || 'relative';
      container.appendChild(btn);
    } else {
      // гарантируем, что кнопка не сабмитит форму
      btn.setAttribute('type', 'button');
    }

    if (!input) {
      console.warn('[register.js] password input not found inside .password-field', container);
      return;
    }

    // Удалим дублирующие обработчики (на случай повторного выполнения)
    // (не всегда нужно, но безопасно)
    try {
      btn.replaceWith(btn.cloneNode(true));
      btn = container.querySelector('.pw-toggle');
    } catch (e) {
      // ignore
    }

    // Навешиваем надёжный обработчик
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const isPwd = input.type === 'password';
      input.type = isPwd ? 'text' : 'password';
      btn.textContent = isPwd ? '👀' : '🙈'; // поменять иконку
      btn.setAttribute('aria-pressed', String(!isPwd));
      // Обновим состояние floating label (если нужно)
      input.dispatchEvent(new Event('input', { bubbles: true }));
      console.log('[register.js] pw-toggle clicked — type is now', input.type);
    });
  });

  console.log('[register.js] init complete');
});


