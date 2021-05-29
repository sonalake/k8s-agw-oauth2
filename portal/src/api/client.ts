export function client(endpoint: string, options?: RequestInit) {
  const headers = {'content-type': 'application/json'}
  let config = undefined;
  if (options) {
    const config = {
      method: options.body ? 'POST' : 'GET',
      headers: {
        ...headers,
        ...options.headers,
      },
      ...options,
    }
    if (options.body) {
      config.body = JSON.stringify(options.body)
    }
  }

  return window
    .fetch(endpoint, config)
    .then(async response => {
      if (response.ok) {
        return await response.json()
      } else {
        const errorMessage = await response.text()
        return Promise.reject(new Error(errorMessage))
      }
    })
}
