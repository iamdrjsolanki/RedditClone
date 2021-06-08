export interface LoginResponsePayload {
    authenticationToken: string;
    username: string;
    expiresAt: Date;
    refreshToken: string;
}