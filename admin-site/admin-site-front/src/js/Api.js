export const callLogin = (username, password) => {
    const credentials = btoa(`${username}:${password}`);
    return fetch(`admin/api/auth`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Basic ${credentials}`
        }
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