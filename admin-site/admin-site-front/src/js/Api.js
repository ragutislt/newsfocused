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

const doFetch = (url, options) => {
    return fetch(`admin/api/${url}`, 
        options
    ).then(response => response.json)
    .catch(
        statusCode => {
            if (statusCode === 401) {
                //setError('Login failed, please check your credentials')
            } else {
                //setError('An unknown error has occured, please contact your administrator')
            }
    })
}