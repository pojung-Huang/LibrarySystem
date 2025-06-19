const fetchBooks = async (params) => {
  try {
    const [field, direction] = sortConfig.value.field.split('_');
    console.log('搜尋參數：', params);

    const response = await axios.get('http://localhost:8080/api/books/simple-search', {
      params: {
        ...params,
        page: currentPage.value - 1,
        size: itemsPerPage.value,
        sortField: field,
        sortDir: direction
      }
    });
  } catch (error) {
    console.error('取得書籍資料時發生錯誤：', error);
  }
};

const performAdvancedSearch = async () => {
  const hasSearchConditions = advancedSearchConditions.value.some(cond => cond.value.trim());
  const hasFilterConditions = yearFrom.value || yearTo.value || selectedCategorySystem.value || selectedLanguages.value;

  if (!hasSearchConditions && !hasFilterConditions) {
    alert('請至少輸入一個搜尋條件');
    return;
  }

  const conditions = advancedSearchConditions.value
    .filter(cond => cond.value.trim())
    .map(cond => ({
      field: cond.field,
      operator: cond.operator,
      value: cond.value.trim()
    }));

  if (yearFrom.value || yearTo.value) {
    if (yearFrom.value) {
      conditions.push({
        field: 'publishdate',
        operator: 'AND',
        value: yearFrom.value.toString()
      });
    }
    if (yearTo.value) {
      conditions.push({
        field: 'publishdate',
        operator: 'AND',
        value: yearTo.value.toString()
      });
    }
  }

  if (selectedCategorySystem.value) {
    conditions.push({
      field: 'categorysystem',
      operator: 'AND',
      value: selectedCategorySystem.value
    });
  }

  if (selectedLanguages.value) {
    conditions.push({
      field: 'language',
      operator: 'AND',
      value: selectedLanguages.value
    });
  }

  const [sortField, sortDir] = sortConfig.value.field.split('_');

  const start = performance.now();

  try {
    console.log('→ Advanced Search Payload:', conditions);
    const response = await axios.post('http://localhost:8080/api/books/advanced-search', conditions, {
      params: {
        page: currentPage.value - 1,
        size: itemsPerPage.value,
        sortField,
        sortDir
      }
    });
  } catch (error) {
    console.error('進階搜尋時發生錯誤：', error);
  }
}; 