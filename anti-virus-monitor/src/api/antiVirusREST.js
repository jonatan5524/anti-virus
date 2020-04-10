import axios from "axios";
import { userScanDone,scheduleScanDone } from "../components/App.js"

const api = axios.create({
    baseURL: "http://localhost:4060"
});


export async function checkActive(component, path) {
    try {
        const response = await api.get(path);
        component.setState({ status: response.data });
   
    } catch (error) {
        component.setState({ status: false });
    }
};

export async function getLogger(component, path) {
    try {
        const response = await api.get(path, { responseType: 'text' });    
        component.setState({ data: response.data }); 

    } catch (error) {
        return error.response;
    }
};

export async function initDirectory(component, path, param) {
    try {
        const response = await api.get(path, { responseType: 'text', params: { path: param}});
        component.setState({ validPath: "true" });
   
    } catch (error) {
        component.setState({ error: error.response.data });
    }
};

export async function startUserScan(component, path) {
    try {
        await api.get(path);  
        userScanDone=true;
    } catch (error) {
        component.setState({ error: error.response.data });
    }
};
