import React from "react";
import * as antiVirus from "./antiVirusREST.js";
import "../components/App.css";


class ScanResult extends React.Component {
    state = { virusPathList: [], suspiciousPathList: [] };
    virusListGET = "/virusFoundList";
    suspiciousListGET = "/virusSuspiciousList";
    virusCount = 0 ;
    suspiciousCount = 0;

    componentDidMount() {
       
        antiVirus.getList(this, this.basePath + this.statusGetPath);
        setInterval(() => antiVirus.getList(this, this.props.basePath + this.virusListGET), 800);
        setInterval(() => antiVirus.getList(this, this.props.basePath + this.suspiciousListGET), 800);
    }

    listToli (list) {
        return list.map( (path)=> {
            return <li value="">{path}</li>
        })
    }

    

    render () {
        const virusli = this.listToli(this.state.virusPathList);
        const suspiciousli = this.listToli(this.state.suspiciousPathList);
        return (
            <div class="ui header resultScan" >
                <ol class="ui list">
                    <div class="ui inverted red segment ">
                        <li value="">viruses found: { this.virusCount }</li>
                    </div>
                    <div class="ui secondary red segment">
                        <ol>
                            { virusli }
                        </ol>
                    </div>
                </ol>       
                <ol class="ui list">
                    <div class="ui inverted yellow segment ">
                        <li value="">suspicious viruses found: { this.suspiciousCount }</li>
                    </div>
                    <div class="ui secondary yellow segment">
                        <ol>
                            { suspiciousli }
                        </ol>
                    </div>
                </ol>              
            </div>
        );
    }
}


class Logger extends React.Component{
    logEnd = React.createRef();
    loggerGetPath = "/log";
    state = { data: "" };
    btnAutoScroll = true;
    autoScoll = true;

    

    componentDidMount() {   
        antiVirus.getLogger(this, this.props.basePath + this.loggerGetPath);       
        setInterval(() => antiVirus.getLogger(this, this.props.basePath + this.loggerGetPath), 800);
    } 


    render () {
        const data = this.state.data;
        const buttonText = this.btnAutoScroll ? "disable auto scroll" : "enable auto scroll";
        return (    
            <div>
                <button class="ui button" onClick={ ()=> { this.btnAutoScroll = !this.btnAutoScroll}}> { buttonText }</button>
                <br /><br />
                <div>
                    <div class="ui tertiary segment" id="log" >
                        <p class="ui black"> {data} </p>
                        <div class="log scroll" style={{ float:"left", clear: "both" }}
                            ref={(el) => { this.logEnd = el; }}>
                        </div>
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
                <ScanResult basePath={ this.basePath }/>

            </div>
        );
    }

    notActiveScan(){
        return (
        <div>
            <h3 class={`ui large header red`}> Not Active </h3>
            <ScanResult basePath={ this.basePath }/>
        </div>
        );
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

export class UserScan extends Scanner {
    scannerType = "User scan status:";
    basePath = "/userScan";

    notActiveScan () {

        return (
            <div>
                <div>
                    <h3 class={`ui large header red`}> Not Active </h3>
                    <InitUserScan basePath={ this.basePath } /> 
                    <ScanResult basePath={ this.basePath }/>
                </div>

            </div>

        );
    }
    
}

export class ScheduleScan  extends Scanner {
    scannerType = "Schedule scan status:";
    basePath = "/scheduleScan";
   
}