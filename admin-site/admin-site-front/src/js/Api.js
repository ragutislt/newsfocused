export const callLogin = (username, password) => {
    return fetch(`admin/api/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username,
            password
        })
    })
}
