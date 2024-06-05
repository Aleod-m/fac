// Clamp val between min and max.
export function clamp(min:number, max:number, val:number) {
    return Math.min(max, Math.max(min, val));
}

// Linear interpolation.
export function lerp(a: number, b:number, t:number) {
    return (b - a) * t + a;
}

// Clamped linear interpolation.
export function clerp(a:number, b:number, t:number) {
   return clamp(a, b, lerp(a, b, t)); 
}

// Inverse linear interpolation
export function ilerp(a:number, b:number, val:number) {
    return (val - a) / (b - a);
}

// Inverse clamped linear interpolation
export function iclerp(a:number, b:number, t:number) {
   return clamp(0, 1, ilerp(a, b, t)); 
}

// Mapping an inteval onto another
export function map(ia:number, ib:number, fa:number, fb:number, val:number) {
    return lerp(fa, fb, ilerp(ia, ib, val));
}

// Mapping an inteval onto another with clamping
export function cmap(ia:number, ib:number, fa:number, fb:number, val:number) {
    return lerp(fa, fb, iclerp(ia, ib, val));
}
