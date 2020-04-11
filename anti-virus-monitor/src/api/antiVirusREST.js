import axios from "axios";

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
        await api.get(path, { responseType: 'text', params: { path: param}});
        component.setState({ validPath: "true" });
   
    } catch (error) {
        console.log(error.response);
    }
};

export async function startUserScan(component, path) {
    try {
        await api.get(path);  
    } catch (error) {
        console.log(error.response);
    }
};

export async function getList(component, path) {
    try {
        const response = await api.get(path, { responseType: "json" });  
        if (path.includes("virusFoundList")){
            component.virusCount = response.data.length;
            component.setState( {virusPathList: response.data} );
        }
        else {
            component.suspiciousCount = response.data.length;
            component.setState({ suspiciousPathList: response.data });
        }

    } catch (error) {
        console.log(error );
    }
};
