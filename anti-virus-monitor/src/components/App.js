import React from "react";
import antiVirus from "../api/antiVirus.js";
import "./App.css";

const ScanActiveStatus = {
    ACTIVE: {
        header: "Active",
        color: "green"
    },
    NOT_ACTIVE: {
        header: "Not Active",
        color: "red"
    }
};

class UserScan extends React.Component {
    state = { status: "" };
    
    checkActive = async () => {
        try {
            const response = await antiVirus.get("/userScan/status");
            if(response.data){        
                this.setState({ status: ScanActiveStatus["ACTIVE"] });
            }
            else{
                this.setState({ status: ScanActiveStatus["NOT_ACTIVE"] });
            }
            
        } catch (error) {
             this.setState({ status: ScanActiveStatus["NOT_ACTIVE"] });
        }
    };
        
    componentDidMount() {
        setInterval(() => this.checkActive(), 800);
    } 
    
    render () {
        const { header, color } = this.state.status;
        
        return (
             <div>
                <h3 class="ui large dividing header"> User Scan status: </h3>
                <h3 class={`ui large header ${color}`}>{header}</h3>
            </div>
        );
    }
}

class ScheduleScan extends React.Component {
    state = { status: "" };

    checkActive = async () => {
        try {
            const response = await antiVirus.get("/scheduleScan/status");
            if(response.data){        
                this.setState({ status: ScanActiveStatus["ACTIVE"] });
            }
            else{
                this.setState({ status: ScanActiveStatus["NOT_ACTIVE"] });
            }
            
        } catch (error) {
            this.setState({ status: ScanActiveStatus["NOT_ACTIVE"] });
        }
    };

    componentDidMount() {
        setInterval(() => this.checkActive(), 800);
    } 

    render () {
        const { header, color } = this.state.status;
        
        return (
            <div>
                <h3 class="ui large dividing header"> Schedule scan status: </h3>
                <h3 class={`ui large header ${color}`}>{header}</h3>         
            </div>
        );
    }
}

class App extends React.Component {

    render () {

        return ( 
            <div class ="main">
                <div class="ui inverted segment ">
                    <div class="ui huge header " id = "AntiVirusHeader"  > Anti Virus </div>
                </div>
                <div class="ui segment">
                    <div class="ui two column very relaxed grid">
                        <div class="column" >
                           <ScheduleScan/>
                        </div>
                        <div class="column" >
                           <UserScan/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
