# Api definition

## Web Api

### Images

| Request Type | route                              | response                                                                           |
|--------------|------------------------------------| ---------------------------------------------------------------------------------- |
| post         | /login?user={name}&pwd={passWord}  | status                                                                             |
| delete       | /logout?user={name}                | status                                                                             |
| post         | /register?user={name}&pwd={passWord} | status                                                                             |
| get          | /{user}/images                     | `{id: long, name: string, type: MediaType, size: {w: int, h:int, chanels: int }} ` |
| get          | /{user}/images/{id}                | image in blob format                                                               |
| post         | /{user}/images                     | status                                                                             |
| delete       | /{user}/images                     | status                                                                             |

### Algorithm

| Request Type | route                                              | response                                    |
| ------------ | -------------------------------------------------- | ------------------------------------------- |
| get          | /{user}/images/{id}?{algorithm={name}&{params}}    | image in blob format with algorithm applied |

| algorithm              | name | param         | effect                                              |
| ---------------------- | ---- | ------------- | --------------------------------------------------- |
| Luminosity adjustement | lum  | val={int}     | Shift the value of the hsv color by `val`           |
| Histogram equalizer    | heq  | chan={str}    | Equalize the chanel `chan`. (`s` or `v`)            |
| Colorize               | hue  | col={int}     | Set the hue to `val`.                               |
| Blur                   | blur | method={str}  | Blur the image using gaussian or mean. (`g` or `m`) |
| EdgeDetection          | edge |               | Detect the edges using the sobel kernels.           |
