// Smart Service Booking - App Utilities

function showToast(message, type = 'info') {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
    };

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <i class="${icons[type] || icons.info}"></i>
        <span>${message}</span>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

// Format date
function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

// Format datetime
function formatDateTime(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

// Format currency
function formatCurrency(amount) {
    if (amount === null || amount === undefined) return '৳0';
    return '৳' + Number(amount).toLocaleString('en-IN');
}

// Get status badge HTML
function getStatusBadge(status) {
    const statusMap = {
        'PENDING': 'badge-pending',
        'CONFIRMED': 'badge-confirmed',
        'APPROVED': 'badge-approved',
        'COMPLETED': 'badge-completed',
        'CANCELLED': 'badge-cancelled',
        'REJECTED': 'badge-rejected',
        'BANNED': 'badge-banned'
    };
    const cls = statusMap[status] || 'badge-pending';
    return `<span class="badge ${cls}">${status.replace('_', ' ')}</span>`;
}

// Generate star rating HTML
function getStarRating(rating) {
    let html = '<span class="stars">';
    for (let i = 1; i <= 5; i++) {
        if (i <= Math.floor(rating)) {
            html += '<i class="fas fa-star filled"></i>';
        } else if (i - 0.5 <= rating) {
            html += '<i class="fas fa-star-half-alt filled"></i>';
        } else {
            html += '<i class="far fa-star empty"></i>';
        }
    }
    html += '</span>';
    return html;
}

// Service category icons
function getCategoryIcon(category) {
    const icons = {
        'ELECTRICIAN': 'fas fa-bolt',
        'PLUMBER': 'fas fa-wrench',
        'TUTOR': 'fas fa-graduation-cap',
        'CLEANER': 'fas fa-broom',
        'CARPENTER': 'fas fa-hammer',
        'PAINTER': 'fas fa-paint-roller',
        'MECHANIC': 'fas fa-cog',
        'GARDENER': 'fas fa-leaf',
        'PEST_CONTROL': 'fas fa-bug',
        'APPLIANCE_REPAIR': 'fas fa-tv',
        'HOME_SHIFTING': 'fas fa-truck',
        'AC_REPAIR': 'fas fa-snowflake',
        'OTHER': 'fas fa-tools'
    };
    return icons[category] || 'fas fa-tools';
}

// Category color gradients
function getCategoryGradient(category) {
    const gradients = {
        'ELECTRICIAN': 'linear-gradient(135deg, #FFB800, #FF6B00)',
        'PLUMBER': 'linear-gradient(135deg, #3498DB, #2980B9)',
        'TUTOR': 'linear-gradient(135deg, #6C63FF, #5A52D5)',
        'CLEANER': 'linear-gradient(135deg, #00D9A6, #00B890)',
        'CARPENTER': 'linear-gradient(135deg, #E67E22, #D35400)',
        'PAINTER': 'linear-gradient(135deg, #FF6584, #E84393)',
        'MECHANIC': 'linear-gradient(135deg, #636E72, #2D3436)',
        'GARDENER': 'linear-gradient(135deg, #00B894, #00A381)',
        'PEST_CONTROL': 'linear-gradient(135deg, #D63031, #C0392B)',
        'APPLIANCE_REPAIR': 'linear-gradient(135deg, #0984E3, #0767B2)',
        'HOME_SHIFTING': 'linear-gradient(135deg, #A29BFE, #6C5CE7)',
        'AC_REPAIR': 'linear-gradient(135deg, #74B9FF, #0984E3)',
        'OTHER': 'linear-gradient(135deg, #636E72, #2D3436)'
    };
    return gradients[category] || gradients['OTHER'];
}

// Render service card
function renderServiceCard(service) {
    const category = service.category || 'OTHER';
    return `
        <div class="service-card" onclick="viewService(${service.id})">
            <div class="service-card-image" style="background: ${getCategoryGradient(category)}">
                <i class="${getCategoryIcon(category)}" style="color: rgba(255,255,255,0.9)"></i>
                ${service.featured ? '<span class="badge badge-completed service-card-badge">Featured</span>' : ''}
            </div>
            <div class="service-card-body">
                <div class="service-card-category">${category.replace('_', ' ')}</div>
                <h3 class="service-card-title">${service.title}</h3>
                <p class="service-card-desc">${service.description || 'No description available'}</p>
                <div class="service-card-meta">
                    <div class="service-card-price">
                        ${formatCurrency(service.price)} <small>/service</small>
                    </div>
                    <div class="service-card-rating">
                        <i class="fas fa-star"></i>
                        ${service.averageRating ? service.averageRating.toFixed(1) : '0.0'}
                        <span>(${service.totalBookings || 0})</span>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Sidebar toggle (mobile)
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.sidebar-overlay');
    sidebar.classList.toggle('open');
    if (overlay) overlay.classList.toggle('active');
}

// Close sidebar on overlay click
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('sidebar-overlay')) {
        toggleSidebar();
    }
});

// Set sidebar active link
function setActiveLink(page) {
    document.querySelectorAll('.sidebar-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-page') === page) {
            link.classList.add('active');
        }
    });
}

// Tab functionality
function initTabs() {
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const tabGroup = btn.closest('.tabs');
            const contentGroup = tabGroup.nextElementSibling?.parentElement;

            tabGroup.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const target = btn.getAttribute('data-tab');
            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });
            const targetEl = document.getElementById(target);
            if (targetEl) targetEl.classList.add('active');
        });
    });
}

// Load sidebar user info
function loadSidebarUser() {
    const user = Auth.getUser();
    if (!user) return;

    const avatar = document.querySelector('.avatar') || document.getElementById('userAvatar');
    const username = document.querySelector('.name') || document.getElementById('userName');
    const role = document.querySelector('.user-details .role');

    if (avatar) avatar.textContent = Auth.getInitials();
    if (username) username.textContent = user.fullName;
    if (role) role.textContent = user.role.replace('ROLE_', '');
}

// Initialize common features
document.addEventListener('DOMContentLoaded', () => {
    loadSidebarUser();
    initTabs();
});

// Confirm dialog
function confirmAction(message) {
    return confirm(message);
}

// Debounce
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// View service detail (used in browse pages)
function viewService(id) {
    window.location.href = `/frontend/user/book-service.html?id=${id}`;
}
