import React from "react";
import * as antiVirus from "./antiVirusREST.js";
import "../components/App.css";

const userScanDone = true;
const scheduleScanDone = true;

class Logger extends React.Component{
    logEnd = React.createRef();
    loggerGetPath = "/log";
    state = { data: "" };
    autoScroll = true;
    

    componentDidMount() {
        antiVirus.getLogger(this, this.props.basePath + this.loggerGetPath);
        setInterval(() => antiVirus.getLogger(this, this.props.basePath + this.loggerGetPath), 800);
    } 

    componentDidUpdate() {
        this.scrollToBottom();
    }

    scrollToBottom = () => {
        if(this.autoScroll){
            this.logEnd.scrollIntoView({ behavior: "smooth" });
        }
    }

    render () {
        const data = this.state.data;
        const buttonText = this.autoScroll ? "disable auto scroll" : "enable auto scroll";
        return (    
            <div>
                <button class="ui button" onClick={ ()=> { this.autoScroll = !this.autoScroll}}> { buttonText }</button>
                <div class="ui tertiary segment" id="log" >
                    <p class="ui black"> {data} </p>
                    <div style={{ float:"left", clear: "both" }}
                        ref={(el) => { this.logEnd = el; }}>
                    </div>
                </div>
            </div>

        );
    }
}

class Scanner extends React.Component {
    state = { status: "" };
    scannerType = "";
    basePath = "";
    statusGetPath = "/status";

    componentDidMount() {
        antiVirus.checkActive(this, this.basePath + this.statusGetPath);
        setInterval(() => antiVirus.checkActive(this, this.basePath + this.statusGetPath), 800);
    }

    isActive(status) {
        if (status) {
            return this.activeScan();
        }
        else {
            return this.notActiveScan();
        }
    }

    activeScan(){
        return (
            <div>
                <h3 class={`ui large header green`}> Active </h3>
                <Logger basePath={ this.basePath }/>
                
            </div>
        );
    }

    notActiveScan(){
        return <h3 class={`ui large header red`}> Not Active </h3>;
    }
    
    render () {
        const activeHeader = this.isActive(this.state.status);
        return (
            <div>
                <h3 class="ui large dividing header"> { this.scannerType } </h3>
                { activeHeader }
            </div>

        );
    }
}

class InitUserScan extends React.Component {
    initDirectoryGetPath = "/initDirectoryPathScan";
    startScanGetPath = "/start";
    input = "";
    state = { validPath: "false", input: "", error: ""};

    handleChange = (e) =>  {
        this.input = e.target.value;
    }

    handleClick() {
        antiVirus.initDirectory(this, this.props.basePath + this.initDirectoryGetPath, this.input);
    }

    validInputCheck(valid) {
        if (this.state.error.startsWith("invalid") && valid != "true" ) {
            return(
                <div class="ui pointing below label red">
                    path not found!
                </div>
            );
        }
        else if(valid == "false") {
            return(
                <div class="ui pointing below label">
                    Enter folder to scan
                </div>
            );
        }
        else {
            return(
                <div class="ui right pointing label green">
                    valid path
                </div>
            );
        }
    }

    startScan() {
        antiVirus.startUserScan(this, this.props.basePath + this.startScanGetPath);
    }

    render () {
        const valid = this.state.validPath;
        
        return (
            <div>
                { this.validInputCheck(valid) }
                <div class={`ui ${ (valid=="true")? "disabled" : "" } input focus`}>
                    <input id="initDirectoryInput" type="text" onChange={ this.handleChange } placeholder="init directory" />
                </div>
                <div>
                <input type="button" class={`ui green ${ (valid=="true")? "disabled" : "" } button`}
                                     onClick={() => {this.handleClick() }} value="submit" />
                </div>
                <br />
                <div>
                <input type="button" class={`ui green ${ (valid=="false")? "disabled" : "" } button`}
                                     onClick={() => {this.startScan() }} value="start scan" />
                </div>
            </div>

        );
    }
}

class scanResult extends React.Component {
    


    render () {
        return (
            <div>

            </div>
        );
    }
}

export class UserScan extends Scanner {
    scannerType = "User scan status:";
    basePath = "/userScan";

    notActiveScan () {
        const superReturnVal = super.notActiveScan();
        let elem = ""
        if(this.userScanDone) {
            elem =  <scanResult basePath={ this.basePath } />
        }
        else {
            elem =  <InitUserScan basePath={ this.basePath } />
        }

        return (
            <div>
                <div>
                    { superReturnVal }
                    { elem }
                </div>

            </div>

        );
    }
    
}

export class ScheduleScan  extends Scanner {
    scannerType = "Schedule scan status:";
    basePath = "/scheduleScan";
   
}