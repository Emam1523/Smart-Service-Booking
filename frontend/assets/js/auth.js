// Smart Service Booking - Auth Helper

class Auth {
    static isLoggedIn() {
        return !!localStorage.getItem('token');
    }

    static getUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }

    static getRole() {
        const user = this.getUser();
        return user ? user.role : null;
    }

    static getUserId() {
        const user = this.getUser();
        return user ? user.userId : null;
    }

    static getProviderId() {
        const user = this.getUser();
        return user ? user.providerId : null;
    }

    static getToken() {
        return localStorage.getItem('token');
    }

    static setAuth(authResponse) {
        localStorage.setItem('token', authResponse.token);
        localStorage.setItem('user', JSON.stringify({
            userId: authResponse.userId,
            fullName: authResponse.fullName,
            email: authResponse.email,
            role: authResponse.role,
            providerId: authResponse.providerId
        }));
    }

    static logout() {
        localStorage.clear();
        window.location.href = '/frontend/index.html';
    }

    static redirectToDashboard() {
        const role = this.getRole();
        if (role === 'ADMIN') window.location.href = '/frontend/admin/dashboard.html';
        else if (role === 'PROVIDER') window.location.href = '/frontend/provider/dashboard.html';
        else window.location.href = '/frontend/user/dashboard.html';
    }

    static requireAuth(role) {
        if (!this.isLoggedIn()) {
            window.location.href = '/frontend/user/login.html';
            return false;
        }
        if (role && this.getRole() !== role) {
            this.redirectToDashboard();
            return false;
        }
        return true;
    }

    static getInitials() {
        const user = this.getUser();
        if (!user || !user.fullName) return '?';
        return user.fullName.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
    }
}
