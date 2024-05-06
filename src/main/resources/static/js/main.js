function saveUser(user) {
    let userAsJson = JSON.stringify(user)
    localStorage.setItem('user', userAsJson)
}