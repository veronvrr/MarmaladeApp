// listening-transcript.js
(function(){
  function init() {
    const btn = document.getElementById('toggleTranscriptBtn');
    const block = document.getElementById('transcriptBlock');
    if (!btn || !block) return;
    let shown = false;
    // toggle on custom event (first load -> toggle once)
    document.addEventListener('toggleTranscriptRequested', () => {
      toggle();
    });
    btn.addEventListener('click', toggle);
    function toggle(){
      shown = !shown;
      block.style.display = shown ? 'block' : 'none';
      btn.textContent = shown ? 'Скрыть транскрипт' : 'Показать транскрипт';
    }
  }
  // экспортируем и инициализируем
  window.initTranscriptToggle = init;
  // можно инициализировать сразу, но мы не вызываем init(), чтобы не показывать ничего до загрузки
})();
