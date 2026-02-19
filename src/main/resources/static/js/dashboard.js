// dashboard.js — safe initializer with server-activeTab support
document.addEventListener('DOMContentLoaded', () => {
  try {
    console.log('dashboard: safe init');

    const items = Array.from(document.querySelectorAll('.sidebar-item'));
    const contents = Array.from(document.querySelectorAll('.tab-content'));

    if (!items.length || !contents.length) {
      console.warn('dashboard: no sidebar items or tab contents found', { items: items.length, contents: contents.length });
      contents.forEach(ct => ct.classList.add('show'));
      return;
    }

    function setActiveTab(name) {
      items.forEach(it => {
        const active = it.dataset.tab === name;
        it.classList.toggle('active', active);
        it.setAttribute('aria-selected', active ? 'true' : 'false');
        if (active) it.focus();
      });
      contents.forEach(ct => {
        const show = ct.dataset.tabContent === name;
        ct.classList.toggle('show', show);
        ct.setAttribute('aria-hidden', show ? 'false' : 'true');
      });
      try { history.replaceState(null, '', `#${name}`); } catch(e){}
    }

    // keyboard+click handlers for sidebar
    items.forEach((it, idx) => {
      it.addEventListener('click', () => setActiveTab(it.dataset.tab));
      it.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setActiveTab(it.dataset.tab); }
        if (e.key === 'ArrowDown' || e.key === 'ArrowRight') {
          e.preventDefault();
          const next = items[(idx + 1) % items.length];
          next.focus();
        }
        if (e.key === 'ArrowUp' || e.key === 'ArrowLeft') {
          e.preventDefault();
          const prev = items[(idx - 1 + items.length) % items.length];
          prev.focus();
        }
      });
    });

    // modules click: update placeholder hint but DO NOT prevent link navigation
    const moduleCards = Array.from(document.querySelectorAll('.module-card'));
    const placeholder = document.getElementById('module-placeholder');
    if (placeholder && moduleCards.length) {
      moduleCards.forEach(btn => {
        btn.addEventListener('click', (e) => {
          const title = btn.querySelector('.module-title')?.textContent || btn.dataset.module || 'модуль';
          placeholder.innerHTML = `<p class="hint">Нажали: ${title}. Сейчас откроется страница модуля.</p>`;
          // не e.preventDefault() — пусть ссылка работает
        });
      });
    }

    // initial tab resolution order:
    // 1) server-provided data-active-tab on .page (if matches an existing item)
    // 2) location.hash (if valid)
    // 3) sidebar-item.active (if server rendered an active class)
    // 4) fallback to first item
    const serverActive = document.querySelector('.page')?.dataset?.activeTab;
    const hash = location.hash ? location.hash.replace('#','') : null;
    const validServer = serverActive && items.some(i => i.dataset.tab === serverActive) ? serverActive : null;
    const validHash = hash && items.some(i => i.dataset.tab === hash) ? hash : null;
    const initial = validServer || validHash || (document.querySelector('.sidebar-item.active')?.dataset.tab) || items[0].dataset.tab;
    if (initial) setActiveTab(initial);

    // optional toast show
    const toast = document.getElementById('toast');
    if (toast) {
      requestAnimationFrame(() => toast.classList.add('show'));
      setTimeout(() => toast.classList.remove('show'), 3000);
    }

  } catch (err) {
    console.error('dashboard: init failed', err);
    document.querySelectorAll('.tab-content').forEach(ct => ct.classList.add('show'));
  }
});
