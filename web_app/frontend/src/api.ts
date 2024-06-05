import axios from 'axios';
import Axios, { AxiosResponse } from 'axios';
import HTMLImageElement, { ref, Ref } from 'vue';
import { ImageI, AlgorithmI } from "@/api_types";

const instance = axios.create({ baseURL: "/", timeout: 15000 });

const responseBody = (response: AxiosResponse) => response.data;

let user: string|undefined = undefined;

export const userLoggedIn = ():string|undefined => {
    return user;
}

const requests = {
    get: (url: string, params?: {}) => instance.get(url, params).then(responseBody),
    post: (url: string, body: {}) => instance.post(
        url,
        body,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            },
        }).then(responseBody),
    put: (url: string) => instance.put(url).then(responseBody),
    delete: (url: string, body?: {}) => instance.delete(url, body||{}).then(responseBody)
}

export const Api = {
    PostUserRegister: (username: string, password:string, callback: ()=>void): void => {
        let url= "register";
        let registerForm = new FormData();
        registerForm.append("user", username);
        registerForm.append("pwd", password);
        requests.post(url, registerForm).then((result: any):void => {
            callback();
            user = username;
        });
    },
    PostUserLogin: (username: string, password:string, callback:()=> void ): void => {
        let url= "login";
        let loginForm = new FormData();
        loginForm.append("user", username);
        loginForm.append("pwd", password);
        requests.post(url, loginForm).then((result:any):void => {
            callback();
            user = username;
        });
    },
    DeleteUserLogout: (username: string, callback: ()=>void ): void => {
        let url = "logout";
        let logoutForm = new FormData();
        logoutForm.append("user", username);
        requests.delete(url, logoutForm).then((result:any):void => {
            callback();
            user = undefined;
        });

    },
    GetImageList: (target: Ref<Array<ImageI>>): void => {
        requests.get(user! + "/images").then((result: any): void => {
            target.value = result;
        });
    },
    GetImage: (id: number, target: Ref<String>, algo?: string, callback?: (() => void)): void => {
        var url: string = user! + `/images/${id}`;
        if (algo) {
            url += "?" + algo;
        }
        if (id >= 0) {
            requests.get(url, { responseType: "blob" })
                .then((value): void => {
                    const reader = new window.FileReader();
                    reader.readAsDataURL(value);
                    reader.onload = function () {
                        const imageDataUrl = (reader.result as string);
                        target.value = imageDataUrl;
                    }
                    if (callback) callback();
                });
        } else {
            target.value = "";
        }
    },
    PostImage: (file: File): void => {
        let formData = new FormData();
        formData.append('file', file);
        Axios.post(user! + "/images", formData, {
            headers: {
                'Content-Type': file.type
            }
        })
    },
    DeleteImage: (id: number): void => {
        requests.delete(user! + `/images/${id}`).then();
    },
};


export const algorithms: Array<AlgorithmI> = [
    {
        name: "Lum",
        url: (_: any): string => {
            return `algorithm=lum&val=${_}`
        },
    },
    {
        name: "Heq",
        url: (_: any): string => {
            return `algorithm=heq&chan=${_}`
        },
    },
    {
        name: "Hue",
        url: (_: any): string => {
            return `algorithm=hue&col=${_}`
        }
    },
    {
        name: "Blur",
        url: (_: any): string => {
            return `algorithm=blur&method=${_}`
        },
    },
    {
        name: "Edge",
        url: (_: any) => "algorithm=edge",
    }
]

