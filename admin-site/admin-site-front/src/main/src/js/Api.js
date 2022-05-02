const BASE_URL = "http://localhost:3000"

export const callLogin = (username, password) => {
    return fetch(`${BASE_URL}/api/login`, {
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
