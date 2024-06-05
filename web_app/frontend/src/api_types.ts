export interface ImageI {
    name: string,
    id: number,
};

export interface AlgorithmI {
    name: string,
    url: (_:any) => string,
}
