export interface Reservation{
    id: number,
    hotelName: string,
    dateIn: Date,
    dateOut: Date,
    valid: number,
    userId?: string
}