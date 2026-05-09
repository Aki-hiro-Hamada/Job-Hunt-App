(function () {
  const pad2 = (n) => String(n).padStart(2, '0');

  const isDigits = (s) => /^[0-9]*$/.test(s);

  const clampInt = (value, min, max) => {
    const n = parseInt(value, 10);
    if (Number.isNaN(n)) return null;
    if (n < min) return min;
    if (n > max) return max;
    return n;
  };

  const normalizeYear = (y) => {
    if (!y) return '';
    if (!isDigits(y)) return '';
    if (y.length > 4) y = y.slice(0, 4);
    return y;
  };

  const normalizeMonthDay = (v, max) => {
    if (!v) return '';
    if (!isDigits(v)) return '';
    if (v.length > 2) v = v.slice(0, 2);
    const n = clampInt(v, 1, max);
    if (n == null) return '';
    return String(n);
  };

  const toIsoDateOrEmpty = ({ y, m, d }) => {
    const yy = normalizeYear(y);
    const mm = normalizeMonthDay(m, 12);
    const dd = normalizeMonthDay(d, 31);
    if (yy.length !== 4 || mm.length < 1 || dd.length < 1) return '';
    return `${yy}-${pad2(mm)}-${pad2(dd)}`;
  };

  const fromIsoDate = (iso) => {
    if (!iso) return null;
    const s = String(iso);
    const [y, m, d] = s.split('-');
    if (!y || !m || !d) return null;
    if (y.length !== 4) return null;
    return { y, m: String(parseInt(m, 10)), d: String(parseInt(d, 10)) };
  };

  const getTargets = (root) => {
    const year = root.querySelector('input[data-date-part="year"]');
    const month = root.querySelector('input[data-date-part="month"]');
    const day = root.querySelector('input[data-date-part="day"]');
    const hiddenId = root.getAttribute('data-hidden-id') || '';
    const hidden = hiddenId ? document.getElementById(hiddenId) : null;
    if (!year || !month || !day || !hidden) return null;
    return { year, month, day, hidden };
  };

  const syncHidden = (t) => {
    const iso = toIsoDateOrEmpty({ y: t.year.value, m: t.month.value, d: t.day.value });
    t.hidden.value = iso;
  };

  const moveIfComplete = (el, expectedLen, nextEl) => {
    if (!nextEl) return;
    if (el.value.length >= expectedLen) nextEl.focus();
  };

  const install = (root) => {
    const t = getTargets(root);
    if (!t) return;

    // 初期値復元（編集画面など）
    const init = fromIsoDate(t.hidden.value);
    if (init) {
      t.year.value = init.y;
      t.month.value = init.m;
      t.day.value = init.d;
    }

    const onInputYear = () => {
      const v = normalizeYear(t.year.value);
      if (t.year.value !== v) t.year.value = v;
      syncHidden(t);
      moveIfComplete(t.year, 4, t.month);
    };

    const onInputMonth = () => {
      // 月は 1-12 に丸める。ただし入力途中の空は許容
      if (!isDigits(t.month.value)) t.month.value = t.month.value.replace(/[^0-9]/g, '');
      if (t.month.value.length > 2) t.month.value = t.month.value.slice(0, 2);
      syncHidden(t);
      moveIfComplete(t.month, 2, t.day);
    };

    const onInputDay = () => {
      if (!isDigits(t.day.value)) t.day.value = t.day.value.replace(/[^0-9]/g, '');
      if (t.day.value.length > 2) t.day.value = t.day.value.slice(0, 2);
      syncHidden(t);
    };

    const onBlurNormalize = () => {
      // blur 時に範囲内に丸める（0 や 99 などを自然な値に）
      t.year.value = normalizeYear(t.year.value);
      t.month.value = normalizeMonthDay(t.month.value, 12);
      t.day.value = normalizeMonthDay(t.day.value, 31);
      syncHidden(t);
    };

    t.year.addEventListener('input', onInputYear);
    t.month.addEventListener('input', onInputMonth);
    t.day.addEventListener('input', onInputDay);

    t.year.addEventListener('blur', onBlurNormalize);
    t.month.addEventListener('blur', onBlurNormalize);
    t.day.addEventListener('blur', onBlurNormalize);

    // Backspace で前パートへ戻る
    t.month.addEventListener('keydown', (e) => {
      if (e.key === 'Backspace' && t.month.value.length === 0) t.year.focus();
    });
    t.day.addEventListener('keydown', (e) => {
      if (e.key === 'Backspace' && t.day.value.length === 0) t.month.focus();
    });
  };

  const boot = () => {
    document.querySelectorAll('[data-date-split="true"]').forEach(install);
  };

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', boot);
  } else {
    boot();
  }
})();

