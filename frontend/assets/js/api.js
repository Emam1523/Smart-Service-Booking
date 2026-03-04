// Smart Service Booking - API Service

const API_BASE = 'http://localhost:8080/api';

class ApiService {
    static getToken() {
        return localStorage.getItem('token');
    }

    static getHeaders(auth = true) {
        const headers = { 'Content-Type': 'application/json' };
        if (auth && this.getToken()) {
            headers['Authorization'] = `Bearer ${this.getToken()}`;
        }
        return headers;
    }

    static async request(url, method = 'GET', body = null, auth = true) {
        const config = {
            method,
            headers: this.getHeaders(auth),
        };
        if (body) config.body = JSON.stringify(body);

        try {
            const response = await fetch(`${API_BASE}${url}`, config);

            // Handle 401/403 BEFORE parsing body (Spring Security may return empty body)
            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    if (auth) {
                        console.warn(`Auth failed (${response.status}) for ${method} ${url}`);
                        localStorage.clear();
                        window.location.href = '/frontend/user/login.html';
                        return;
                    }
                }
                // Try to parse error body
                let errorMsg = 'Something went wrong';
                try {
                    const errData = await response.json();
                    errorMsg = errData.message || errorMsg;
                } catch (e) { /* empty or non-JSON body */ }
                throw new Error(errorMsg);
            }

            const data = await response.json();
            return data;
        } catch (error) {
            if (error.message === 'Failed to fetch') {
                throw new Error('Server is not running. Please start the backend server.');
            }
            throw error;
        }
    }

    // Auth
    static login(email, password) {
        return this.request('/auth/login', 'POST', { email, password }, false);
    }

    static register(data) {
        return this.request('/auth/register', 'POST', data, false);
    }

    // User
    static getProfile() {
        return this.request('/users/me');
    }

    static updateProfile(data) {
        return this.request('/users/me', 'PUT', data);
    }

    static changePassword(oldPassword, newPassword) {
        return this.request('/users/me/password', 'PUT', { oldPassword, newPassword });
    }

    // Services
    static getServices() {
        return this.request('/services', 'GET', null, false);
    }

    static getServicesByCategory(category) {
        return this.request(`/services/category/${category}`, 'GET', null, false);
    }

    static searchServices(query) {
        return this.request(`/services/search?q=${encodeURIComponent(query)}`, 'GET', null, false);
    }

    // Providers
    static getProviders() {
        return this.request('/providers', 'GET', null, false);
    }

    // Bookings
    static createBooking(data) {
        return this.request('/bookings', 'POST', data);
    }

    static getMyBookings() {
        return this.request('/bookings/my');
    }

    static cancelBooking(id, reason) {
        return this.request(`/bookings/${id}/cancel`, 'PUT', { reason });
    }

    // Reviews
    static createReview(data) {
        return this.request('/bookings/review', 'POST', data);
    }

    static getReviewsByProvider(providerId) {
        return this.request(`/reviews/provider/${providerId}`, 'GET', null, false);
    }

    // Provider Dashboard
    static getProviderProfile() {
        return this.request('/provider/me');
    }

    static updateProviderProfile(data) {
        return this.request('/provider/me', 'PUT', data);
    }

    static getProviderServices() {
        return this.request('/provider/services');
    }

    static createProviderService(data) {
        return this.request('/provider/services', 'POST', data);
    }

    static updateProviderService(id, data) {
        return this.request(`/provider/services/${id}`, 'PUT', data);
    }

    static toggleServiceAvailability(id) {
        return this.request(`/provider/services/${id}/toggle`, 'PUT');
    }

    static deleteProviderService(id) {
        return this.request(`/provider/services/${id}`, 'DELETE');
    }

    static getProviderBookings() {
        return this.request('/provider/bookings');
    }

    static acceptBooking(id) {
        return this.request(`/provider/bookings/${id}/accept`, 'PUT');
    }

    static rejectBooking(id, reason) {
        return this.request(`/provider/bookings/${id}/reject`, 'PUT', { reason });
    }

    static completeBooking(id) {
        return this.request(`/provider/bookings/${id}/complete`, 'PUT');
    }

    static getProviderReviews() {
        return this.request('/provider/reviews');
    }

    static getProviderStats() {
        return this.request('/provider/stats');
    }

    // Admin
    static getAdminDashboard() {
        return this.request('/admin/dashboard');
    }

    static getAdminUsers() {
        return this.request('/admin/users');
    }

    static searchAdminUsers(query) {
        return this.request(`/admin/users/search?q=${encodeURIComponent(query)}`);
    }

    static banUser(id) {
        return this.request(`/admin/users/${id}/ban`, 'PUT');
    }

    static unbanUser(id) {
        return this.request(`/admin/users/${id}/unban`, 'PUT');
    }

    static deleteUser(id) {
        return this.request(`/admin/users/${id}`, 'DELETE');
    }

    static getAdminProviders() {
        return this.request('/admin/providers');
    }

    static getPendingProviders() {
        return this.request('/admin/providers/pending');
    }

    static approveProvider(id) {
        return this.request(`/admin/providers/${id}/approve`, 'PUT');
    }

    static rejectProvider(id) {
        return this.request(`/admin/providers/${id}/reject`, 'PUT');
    }

    static banProvider(id) {
        return this.request(`/admin/providers/${id}/ban`, 'PUT');
    }

    static deleteAdminProvider(id) {
        return this.request(`/admin/providers/${id}`, 'DELETE');
    }

    static getAdminServices() {
        return this.request('/admin/services');
    }

    static deleteAdminService(id) {
        return this.request(`/admin/services/${id}`, 'DELETE');
    }

    static getAdminBookings() {
        return this.request('/admin/bookings');
    }

    static getAdminBookingsByStatus(status) {
        return this.request(`/admin/bookings/status/${status}`);
    }

    static getAdminReviews() {
        return this.request('/admin/reviews');
    }

    static deleteAdminReview(id) {
        return this.request(`/admin/reviews/${id}`, 'DELETE');
    }
}
