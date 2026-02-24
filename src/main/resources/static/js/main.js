// ===== API Configuration =====
const API_BASE = '/api';

// ===== Helper Functions =====
async function apiGet(endpoint) {
  try {
    const response = await fetch(`${API_BASE}${endpoint}`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return await response.json();
  } catch (error) {
    console.error(`API GET ${endpoint} failed:`, error);
    return null;
  }
}

async function apiPost(endpoint, data) {
  try {
    const response = await fetch(`${API_BASE}${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    const text = await response.text();
    try { return JSON.parse(text); } catch { return text; }
  } catch (error) {
    console.error(`API POST ${endpoint} failed:`, error);
    return null;
  }
}

async function apiDelete(endpoint) {
  try {
    const response = await fetch(`${API_BASE}${endpoint}`, { method: 'DELETE' });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    return await response.text();
  } catch (error) {
    console.error(`API DELETE ${endpoint} failed:`, error);
    return null;
  }
}

// ===== Mobile Navigation Toggle =====
document.addEventListener('DOMContentLoaded', () => {
  const navToggle = document.querySelector('.nav-toggle');
  const navLinks = document.querySelector('.nav-links');

  if (navToggle && navLinks) {
    navToggle.addEventListener('click', () => {
      navLinks.classList.toggle('active');
      navToggle.classList.toggle('active');
    });

    navLinks.querySelectorAll('a').forEach(link => {
      link.addEventListener('click', () => {
        navLinks.classList.remove('active');
        navToggle.classList.remove('active');
      });
    });
  }

  // ===== Detect Which Page and Initialize =====
  const path = window.location.pathname;

  if (path.includes('inventory')) {
    initInventoryPage();
  } else if (path.includes('prescriptions')) {
    initPrescriptionsPage();
  } else if (path.includes('orders')) {
    initOrdersPage();
  } else if (path.includes('reports')) {
    initReportsPage();
  } else if (path.includes('contact')) {
    initContactPage();
  }

  // ===== Add CSS animation for notifications =====
  const style = document.createElement('style');
  style.textContent = `
    @keyframes slideIn {
      from { opacity: 0; transform: translateX(100%); }
      to { opacity: 1; transform: translateX(0); }
    }
    .nav-toggle.active span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
    .nav-toggle.active span:nth-child(2) { opacity: 0; }
    .nav-toggle.active span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }
    .modal-overlay {
      position: fixed; top: 0; left: 0; width: 100%; height: 100%;
      background: rgba(0,0,0,0.5); z-index: 9998;
      display: flex; align-items: center; justify-content: center;
    }
    .modal {
      background: white; border-radius: 12px; padding: 32px;
      max-width: 500px; width: 90%; max-height: 80vh; overflow-y: auto;
      box-shadow: 0 20px 60px rgba(0,0,0,0.2);
    }
    .modal h2 { margin-bottom: 20px; font-family: 'Inter', sans-serif; font-size: 1.3rem; }
    .modal .form-group { margin-bottom: 16px; }
    .modal .form-group label { display: block; font-size: 0.9rem; font-weight: 500; margin-bottom: 4px; }
    .modal .form-group input, .modal .form-group select {
      width: 100%; padding: 10px 14px; border-radius: 8px;
      border: 1px solid #e8eded; font-size: 0.95rem;
    }
    .modal-actions { display: flex; gap: 12px; margin-top: 20px; justify-content: flex-end; }
    .modal-actions .btn { padding: 10px 24px; border-radius: 8px; font-weight: 600; cursor: pointer; border: none; }
    .modal-actions .btn-cancel { background: #f0f0f0; color: #555; }
    .modal-actions .btn-save { background: #2a9d8f; color: white; }
    .btn-add-new {
      background: #2a9d8f; color: white; padding: 10px 20px;
      border-radius: 8px; font-weight: 600; cursor: pointer; border: none;
      display: inline-flex; align-items: center; gap: 8px; font-size: 0.9rem;
    }
    .btn-add-new:hover { background: #1f7a6f; }
  `;
  document.head.appendChild(style);

  // ===== Animate elements on scroll =====
  const observerOptions = { threshold: 0.1, rootMargin: '0px 0px -50px 0px' };
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = '1';
        entry.target.style.transform = 'translateY(0)';
        observer.unobserve(entry.target);
      }
    });
  }, observerOptions);

  const animateElements = document.querySelectorAll(
    '.feature-card, .stat-card, .prescription-card, .report-card, .why-choose-item'
  );
  animateElements.forEach((el, index) => {
    el.style.opacity = '0';
    el.style.transform = 'translateY(20px)';
    el.style.transition = `all 0.5s ease ${index * 0.1}s`;
    observer.observe(el);
  });
});

// ===================================================================
// INVENTORY PAGE
// ===================================================================
async function initInventoryPage() {
  // Load stats
  const stats = await apiGet('/stats/inventory');
  if (stats) {
    const statValues = document.querySelectorAll('.stat-value');
    if (statValues[0]) statValues[0].textContent = stats.totalItems || 0;
    if (statValues[1]) statValues[1].textContent = stats.lowStock || 0;
    if (statValues[2]) statValues[2].textContent = stats.categories || 0;
    if (statValues[3]) statValues[3].textContent = stats.expiringSoon || 0;
  }

  // Load medications into table
  const medications = await apiGet('/medication');
  const tbody = document.querySelector('#inventoryTable tbody');
  if (medications && tbody) {
    tbody.innerHTML = '';
    if (medications.length === 0) {
      tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:#8e9eab;">No medications found. Add one to get started.</td></tr>';
    } else {
      medications.forEach(med => {
        const isLowStock = (med.medicationstockqty || 0) < 20;
        const expiryDate = med.medicationexpirydate ? new Date(med.medicationexpirydate) : null;
        const now = new Date();
        const daysUntilExpiry = expiryDate ? Math.ceil((expiryDate - now) / (1000 * 60 * 60 * 24)) : 999;
        const isExpiringSoon = daysUntilExpiry <= 90 && daysUntilExpiry > 0;
        const isExpired = daysUntilExpiry <= 0;

        let statusBadge;
        if (isExpired) statusBadge = '<span class="badge badge-red">Expired</span>';
        else if (isExpiringSoon) statusBadge = '<span class="badge badge-red">Expiring Soon</span>';
        else if (isLowStock) statusBadge = '<span class="badge badge-orange">Low Stock</span>';
        else statusBadge = '<span class="badge badge-green">In Stock</span>';

        const categoryBadgeClass = getCategoryBadgeClass(med.medicationcategory);

        const row = document.createElement('tr');
        row.innerHTML = `
          <td>
            <div class="medicine-name">${escapeHtml(med.medicationame || '')}</div>
            <div class="medicine-brand">${escapeHtml(med.suppliername || 'N/A')}</div>
          </td>
          <td><span class="badge ${categoryBadgeClass}">${escapeHtml(med.medicationcategory || 'N/A')}</span></td>
          <td>${med.medicationstockqty || 0}</td>
          <td>$${parseFloat(med.medicationprice || 0).toFixed(2)}</td>
          <td>${med.medicationexpirydate || 'N/A'}</td>
          <td>${statusBadge}</td>
          <td>
            <div class="action-btns">
              <button class="btn-action btn-delete" title="Delete" data-id="${med.medicationid}"><i class="fas fa-trash"></i></button>
            </div>
          </td>
        `;
        tbody.appendChild(row);
      });
    }

    // Attach delete handlers
    tbody.querySelectorAll('.btn-delete').forEach(btn => {
      btn.addEventListener('click', async (e) => {
        const id = e.currentTarget.dataset.id;
        if (confirm('Are you sure you want to delete this medication?')) {
          const result = await apiDelete(`/medication/${id}`);
          if (result !== null) {
            showNotification('Medication deleted successfully.', 'success');
            initInventoryPage();
          } else {
            showNotification('Failed to delete medication.', 'error');
          }
        }
      });
    });
  }

  // Add "Add Medication" button
  addButtonToSearchBar('Add Medication', () => showAddMedicationModal());

  // Search & filter
  const inventorySearch = document.getElementById('inventorySearch');
  const categoryFilter = document.getElementById('categoryFilter');
  if (inventorySearch) inventorySearch.addEventListener('input', () => filterTable('inventoryTable', inventorySearch, categoryFilter));
  if (categoryFilter) categoryFilter.addEventListener('change', () => filterTable('inventoryTable', inventorySearch, categoryFilter));
}

function showAddMedicationModal() {
  showModal('Add Medication', [
    { name: 'supplierid', label: 'Supplier ID', type: 'number', required: true },
    { name: 'medicationame', label: 'Medication Name', type: 'text', required: true },
    { name: 'medicationcategory', label: 'Category', type: 'text', required: true },
    { name: 'medicationprice', label: 'Price', type: 'number', step: '0.01', required: true },
    { name: 'medicationexpirydate', label: 'Expiry Date', type: 'date', required: true },
    { name: 'medicationstockqty', label: 'Stock Quantity', type: 'number', required: true },
  ], async (data) => {
    data.supplierid = parseInt(data.supplierid);
    data.medicationstockqty = parseInt(data.medicationstockqty);
    const result = await apiPost('/medication', data);
    if (result !== null) {
      showNotification('Medication added successfully!', 'success');
      initInventoryPage();
    } else {
      showNotification('Failed to add medication.', 'error');
    }
  });
}

// ===================================================================
// PRESCRIPTIONS PAGE
// ===================================================================
async function initPrescriptionsPage() {
  const stats = await apiGet('/stats/prescriptions');
  if (stats) {
    const statValues = document.querySelectorAll('.stat-value');
    if (statValues[0]) statValues[0].textContent = stats.totalPrescriptions || 0;
  }

  const prescriptions = await apiGet('/prescription');
  const grid = document.querySelector('.prescription-grid');
  if (prescriptions && grid) {
    grid.innerHTML = '';
    if (prescriptions.length === 0) {
      grid.innerHTML = '<div style="text-align:center;padding:40px;color:#8e9eab;grid-column:1/-1;">No prescriptions found. Add one to get started.</div>';
    } else {
      prescriptions.forEach(rx => {
        const card = document.createElement('div');
        card.className = 'prescription-card';
        card.innerHTML = `
          <div class="prescription-card-header">
            <div>
              <h3>${escapeHtml(rx.patientname || 'Patient #' + rx.patientid)}</h3>
              <div class="prescription-id">ID: RX-${rx.prescriptionid}</div>
            </div>
            <span class="badge badge-green">Active</span>
          </div>
          <div class="prescription-detail">
            <i class="fas fa-user-doctor"></i>
            <span>${escapeHtml(rx.pharmacistname || 'Pharmacist #' + rx.pharmacistid)}</span>
          </div>
          <div class="prescription-detail">
            <i class="fas fa-calendar"></i>
            <span>${rx.prescriptiondate || 'N/A'}</span>
          </div>
          <div class="prescription-detail">
            <i class="fas fa-clock"></i>
            <span>Duration: ${escapeHtml(rx.prescriptionduration || 'N/A')}</span>
          </div>
          <div class="prescription-medications">
            <div class="label">Medication:</div>
            <div class="med-tags">
              <span class="med-tag">${escapeHtml(rx.medicationame || 'Medication #' + rx.medicationid)}</span>
            </div>
          </div>
        `;
        grid.appendChild(card);
      });
    }
  }

  addButtonToSearchBar('Add Prescription', () => showAddPrescriptionModal());

  // Search & filter
  const prescriptionSearch = document.getElementById('prescriptionSearch');
  if (prescriptionSearch) {
    prescriptionSearch.addEventListener('input', () => {
      filterCards('.prescription-grid', '.prescription-card', prescriptionSearch);
    });
  }
}

async function showAddPrescriptionModal() {
  showModal('Add Prescription', [
    { name: 'patientid', label: 'Patient ID', type: 'number', required: true },
    { name: 'medicationid', label: 'Medication ID', type: 'number', required: true },
    { name: 'pharmacistid', label: 'Pharmacist ID', type: 'number', required: true },
    { name: 'prescriptionduration', label: 'Duration', type: 'text', required: true, placeholder: 'e.g. 7 days' },
    { name: 'prescriptiondate', label: 'Date', type: 'date', required: true },
  ], async (data) => {
    data.patientid = parseInt(data.patientid);
    data.medicationid = parseInt(data.medicationid);
    data.pharmacistid = parseInt(data.pharmacistid);
    const result = await apiPost('/prescription', data);
    if (result !== null) {
      showNotification('Prescription added successfully!', 'success');
      initPrescriptionsPage();
    } else {
      showNotification('Failed to add prescription.', 'error');
    }
  });
}

// ===================================================================
// ORDERS (SALES) PAGE
// ===================================================================
async function initOrdersPage() {
  const stats = await apiGet('/stats/orders');
  if (stats) {
    const statValues = document.querySelectorAll('.stat-value');
    if (statValues[0]) statValues[0].textContent = stats.totalOrders || 0;
    if (statValues[3]) statValues[3].textContent = '$' + parseFloat(stats.totalRevenue || 0).toFixed(2);
  }

  const sales = await apiGet('/sales');
  const tbody = document.querySelector('#ordersTable tbody');
  if (sales && tbody) {
    tbody.innerHTML = '';
    if (sales.length === 0) {
      tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;padding:40px;color:#8e9eab;">No sales found. Add one to get started.</td></tr>';
    } else {
      sales.forEach((sale, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td><strong>ORD-${String(sale.salesid).padStart(3, '0')}</strong></td>
          <td>
            <div class="medicine-name">${escapeHtml(sale.patientname || 'Patient #' + sale.patientid)}</div>
            <div class="medicine-brand">${escapeHtml(sale.pharmacistname || '')}</div>
          </td>
          <td>${sale.salesdate || 'N/A'}</td>
          <td>1</td>
          <td>$${parseFloat(sale.totalamount || 0).toFixed(2)}</td>
          <td><span class="badge badge-green">Paid</span></td>
          <td><span class="badge badge-green">Completed</span></td>
          <td>
            <div class="action-btns">
              <button class="btn-action btn-view" title="View"><i class="fas fa-eye"></i></button>
            </div>
          </td>
        `;
        tbody.appendChild(row);
      });
    }
  }

  addButtonToSearchBar('Add Sale', () => showAddSaleModal());

  const orderSearch = document.getElementById('orderSearch');
  const orderStatusFilter = document.getElementById('orderStatusFilter');
  if (orderSearch) orderSearch.addEventListener('input', () => filterTable('ordersTable', orderSearch, orderStatusFilter));
  if (orderStatusFilter) orderStatusFilter.addEventListener('change', () => filterTable('ordersTable', orderSearch, orderStatusFilter));
}

async function showAddSaleModal() {
  showModal('Add Sale', [
    { name: 'pharmacistid', label: 'Pharmacist ID', type: 'number', required: true },
    { name: 'patientid', label: 'Patient ID', type: 'number', required: true },
    { name: 'salesdate', label: 'Sales Date', type: 'date', required: true },
    { name: 'totalamount', label: 'Total Amount', type: 'number', step: '0.01', required: true },
    { name: 'paymentmethod', label: 'Payment Method', type: 'text', required: true, placeholder: 'e.g. Cash, Card' },
  ], async (data) => {
    data.pharmacistid = parseInt(data.pharmacistid);
    data.patientid = parseInt(data.patientid);
    const result = await apiPost('/sales', data);
    if (result !== null) {
      showNotification('Sale added successfully!', 'success');
      initOrdersPage();
    } else {
      showNotification('Failed to add sale.', 'error');
    }
  });
}

// ===================================================================
// REPORTS PAGE
// ===================================================================
async function initReportsPage() {
  const stats = await apiGet('/stats/reports');
  if (stats) {
    const revenueEl = document.getElementById('report-revenue');
    const ordersEl = document.getElementById('report-orders');
    const customersEl = document.getElementById('report-customers');
    const prescriptionsEl = document.getElementById('report-prescriptions');

    if (revenueEl) revenueEl.textContent = '$' + parseFloat(stats.totalRevenue || 0).toLocaleString();
    if (ordersEl) ordersEl.textContent = stats.totalOrders || 0;
    if (customersEl) customersEl.textContent = stats.totalCustomers || 0;
    if (prescriptionsEl) prescriptionsEl.textContent = stats.totalPrescriptions || 0;

    // Update top products
    const container = document.getElementById('top-products-container');
    if (container && stats.topProducts && stats.topProducts.length > 0) {
      container.innerHTML = '';
      stats.topProducts.forEach((product, idx) => {
        const div = document.createElement('div');
        div.className = 'top-product';
        div.innerHTML = `
          <div class="product-rank">${idx + 1}</div>
          <div class="product-info">
            <div class="product-name">${escapeHtml(product.medicationame || '')}</div>
            <div class="product-category">${escapeHtml(product.medicationcategory || '')}</div>
          </div>
          <div class="product-units">
            <div class="units-count">${product.total_sold || 0}</div>
            <div class="units-label">prescriptions</div>
          </div>
        `;
        container.appendChild(div);
      });
    } else if (container) {
      container.innerHTML = '<p style="text-align:center; color:#888; padding:20px;">No product data available.</p>';
    }
  }

  // Time tabs animation
  const timeTabs = document.querySelectorAll('.time-tab');
  timeTabs.forEach(tab => {
    tab.addEventListener('click', () => {
      timeTabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
      const reportCards = document.querySelectorAll('.report-card');
      reportCards.forEach(card => {
        card.style.transform = 'scale(0.95)';
        card.style.opacity = '0.7';
        setTimeout(() => {
          card.style.transform = 'scale(1)';
          card.style.opacity = '1';
        }, 200);
      });
    });
  });
}

// ===================================================================
// CONTACT PAGE
// ===================================================================
function initContactPage() {
  const contactForm = document.getElementById('contactForm');
  if (contactForm) {
    contactForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const fullName = document.getElementById('fullName').value.trim();
      const email = document.getElementById('email').value.trim();
      const message = document.getElementById('message').value.trim();

      if (!fullName || !email || !message) {
        showNotification('Please fill in all required fields.', 'error');
        return;
      }

      const submitBtn = contactForm.querySelector('button[type="submit"]');
      submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';
      submitBtn.disabled = true;

      setTimeout(() => {
        showNotification("Message sent successfully! We'll get back to you soon.", 'success');
        contactForm.reset();
        submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Send Message';
        submitBtn.disabled = false;
      }, 1500);
    });
  }
}

// ===================================================================
// UTILITY FUNCTIONS
// ===================================================================
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function getCategoryBadgeClass(category) {
  if (!category) return 'badge-teal';
  const cat = category.toLowerCase();
  if (cat.includes('antibiotic')) return 'badge-teal';
  if (cat.includes('pain') || cat.includes('relief')) return 'badge-green';
  if (cat.includes('cardio') || cat.includes('heart')) return 'badge-blue';
  if (cat.includes('diabetes')) return 'badge-purple';
  if (cat.includes('gastro')) return 'badge-orange';
  if (cat.includes('allergy')) return 'badge-teal';
  if (cat.includes('vitamin')) return 'badge-green';
  if (cat.includes('respirat')) return 'badge-blue';
  return 'badge-teal';
}

function filterTable(tableId, searchInput, filterSelect) {
  const searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
  const filterVal = filterSelect ? filterSelect.value.toLowerCase() : '';
  const table = document.getElementById(tableId);
  if (!table) return;
  const rows = table.querySelectorAll('tbody tr');

  rows.forEach(row => {
    const text = row.textContent.toLowerCase();
    const matchesSearch = text.includes(searchTerm);
    const matchesFilter = !filterVal || text.includes(filterVal);
    row.style.display = matchesSearch && matchesFilter ? '' : 'none';
  });
}

function filterCards(gridSelector, cardSelector, searchInput) {
  const searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
  const cards = document.querySelectorAll(`${gridSelector} ${cardSelector}`);
  cards.forEach(card => {
    const text = card.textContent.toLowerCase();
    card.style.display = text.includes(searchTerm) ? '' : 'none';
  });
}

function addButtonToSearchBar(label, onClick) {
  const searchBar = document.querySelector('.search-filter-bar');
  if (searchBar && !searchBar.querySelector('.btn-add-new')) {
    const btn = document.createElement('button');
    btn.className = 'btn-add-new';
    btn.innerHTML = `<i class="fas fa-plus"></i> ${label}`;
    btn.addEventListener('click', onClick);
    searchBar.appendChild(btn);
  }
}

function showModal(title, fields, onSave) {
  // Remove any existing modal
  document.querySelector('.modal-overlay')?.remove();

  const overlay = document.createElement('div');
  overlay.className = 'modal-overlay';

  let fieldsHtml = fields.map(f => `
    <div class="form-group">
      <label for="modal-${f.name}">${f.label}</label>
      <input type="${f.type || 'text'}" id="modal-${f.name}" name="${f.name}"
             ${f.required ? 'required' : ''} ${f.step ? `step="${f.step}"` : ''}
             ${f.placeholder ? `placeholder="${f.placeholder}"` : ''}>
    </div>
  `).join('');

  overlay.innerHTML = `
    <div class="modal">
      <h2>${title}</h2>
      <form id="modalForm">
        ${fieldsHtml}
        <div class="modal-actions">
          <button type="button" class="btn btn-cancel" id="modalCancel">Cancel</button>
          <button type="submit" class="btn btn-save">Save</button>
        </div>
      </form>
    </div>
  `;

  document.body.appendChild(overlay);

  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) overlay.remove();
  });

  document.getElementById('modalCancel').addEventListener('click', () => overlay.remove());

  document.getElementById('modalForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = {};
    fields.forEach(f => {
      formData[f.name] = document.getElementById(`modal-${f.name}`).value;
    });
    overlay.remove();
    onSave(formData);
  });
}

// ===== Notification System =====
function showNotification(message, type = 'info') {
  const existing = document.querySelector('.notification');
  if (existing) existing.remove();

  const notification = document.createElement('div');
  notification.className = `notification notification-${type}`;
  notification.innerHTML = `
    <div class="notification-content">
      <i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
      <span>${message}</span>
    </div>
    <button class="notification-close"><i class="fas fa-times"></i></button>
  `;

  Object.assign(notification.style, {
    position: 'fixed', top: '24px', right: '24px', padding: '16px 20px',
    borderRadius: '12px',
    background: type === 'success' ? '#e8f8ef' : type === 'error' ? '#fde8e8' : '#e8f0fe',
    color: type === 'success' ? '#27ae60' : type === 'error' ? '#e74c3c' : '#5b8def',
    boxShadow: '0 4px 20px rgba(0,0,0,0.12)', zIndex: '9999',
    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
    gap: '16px', minWidth: '300px', animation: 'slideIn 0.3s ease',
    fontFamily: 'Inter, sans-serif', fontSize: '0.92rem', fontWeight: '500'
  });

  const content = notification.querySelector('.notification-content');
  Object.assign(content.style, { display: 'flex', alignItems: 'center', gap: '10px' });

  const closeBtn = notification.querySelector('.notification-close');
  Object.assign(closeBtn.style, {
    background: 'none', border: 'none', color: 'inherit',
    cursor: 'pointer', fontSize: '0.9rem', opacity: '0.7', padding: '4px'
  });

  closeBtn.addEventListener('click', () => {
    notification.style.opacity = '0';
    notification.style.transform = 'translateX(100%)';
    setTimeout(() => notification.remove(), 300);
  });

  document.body.appendChild(notification);

  setTimeout(() => {
    if (notification.parentElement) {
      notification.style.opacity = '0';
      notification.style.transform = 'translateX(100%)';
      notification.style.transition = 'all 0.3s ease';
      setTimeout(() => notification.remove(), 300);
    }
  }, 4000);
}
