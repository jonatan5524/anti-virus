import React from "react";
import * as scanners from "../api/scanners.js";

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
                           <scanners.ScheduleScan/>
                        </div>
                        <div class="column" >
                           <scanners.UserScan/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
