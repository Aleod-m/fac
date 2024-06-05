# Developpement logiciel

Groupe m2e:

- David Galland
- Adrien Derobert-Mazure
- Oussama Osmani

[[_TOC_]]

## Known issues
### ImageProcessing.
The image processing library can't handle graylevels file yet.
The edge detection algorithm is not doing what we expect.
### Backend.
There aren't any test for the parameters on the `/images/{id}` route.
### Frontend.
Sometimes the frontend has trouble charging the Edit Page.
The field for inputing the value for the algorithm argument is not checked.
All the metadata are not shown.


## Goals

As of now role repartition is arbitrary.

---

### General:

- [x] Support JPEG and Png
  - [x] backend
    - [x] JPEG
    - [x] PNG
  - [x] imageprocessing
    - [x] JPEG
    - [x] PNG
---

### Backend: (David Galland)

- [x] Initialize the server with an image lib.
  - [x] An error have to occur if image lib is not present.
  - [ ] Create a test folder 2 layers deep with non image file types.
- [x] Manage images:
  - [x] get image by id.
  - [x] delete image by id.
  - [x] add an image by id.
  - [x] get all images metadata.
- [x] Manage fonction call:
  - [x] brightness
  - [x] convolutionRGB
  - [x] rgbToHsv
  - [x] hsvToRgb
---

### FrontEnd: (Adrien Derobert-Mazure)

- [x] Browse images on the server.
- [x] Apply effect on image.
- [x] Save an image on disk.
- [x] Upload an image on the server.
- [x] Delete images on the server.

---

### ImageTreatment: (Oussama Osmani)

- [x] Luminosity adjustement
- [x] Histogram equalizer
- [x] Colored filter
- [x] Blur filter
- [x] Edge filter (Kind of)
---

## Application info:

Sever tested on:

- ArcoLinux (Arch based linux distribution).

Client tested on:

- Firefox

## Api

[Backend Api](api.md)

[Image Processing](imageprocessing/Readme.md)
