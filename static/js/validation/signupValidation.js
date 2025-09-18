// Signup validation schema using Zod
const signupSchema = zod.object({
    username: zod.string().min(3, "사용자명은 3자 이상이어야 합니다").max(20, "사용자명은 20자 이하여야 합니다"),
    email: zod.string().email("올바른 이메일 형식이 아닙니다"),
    password: zod.string().min(8, "비밀번호는 8자 이상이어야 합니다").max(128, "128자 이하여야 합니다."),
    confirmPassword: zod.string(),
    fullName: zod.string().optional(),
    phoneNumber: zod.string().optional()
}).refine(data => data.password === data.confirmPassword, {
    message: "비밀번호가 일치하지 않습니다",
    path: ["confirmPassword"]
});

// Export for use in signup.js
window.signupSchema = signupSchema;
