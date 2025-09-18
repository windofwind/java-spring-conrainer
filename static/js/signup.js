document.getElementById('signupForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const fullName = document.getElementById('fullName').value;
    const phoneNumber = document.getElementById('phoneNumber').value;

    const data = { username, email, password, confirmPassword, fullName, phoneNumber };
    const result = window.signupSchema.safeParse(data);
    if (!result.success) {
        alert(result.error.errors.map(err => err.message).join('\n'));
        return;
    }

    try {
        const response = await axios.post('/users/register', {
            username,
            email,
            password,
            fullName,
            phoneNumber
        });
        alert('회원가입 성공');
        window.location.href = 'login.html';
    } catch (error) {
        console.error('Error:', error);
        alert('회원가입 실패');
    }
});
