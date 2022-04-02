export interface TokenDecoded {
    jti: number
    iss: string
    iat: number
    exp: number
    'http://schemas.microsoft.com/ws/2008/06/identity/claims/role': string[]
    sub: string
    email: string
}