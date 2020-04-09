import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:4060",
    validateStatus: function (status) {
                return status >= 200 && status < 300; // default
    }
});

