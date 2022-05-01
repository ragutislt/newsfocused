const BASE_URL = "http://localhost:3000"

export const login = (username, password) => {
    fetch(`${BASE_URL}/api/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username,
            password
        })
    })
        .then(data => {
            // enter you logic when the fetch is successful
            alert('Successful login')
        })
        .catch(error => {
            // enter your logic for when there is an error (ex. error toast)
            alert('Failed login')
        })
}
