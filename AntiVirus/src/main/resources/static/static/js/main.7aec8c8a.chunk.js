(this["webpackJsonpanti-virus-monitor"]=this["webpackJsonpanti-virus-monitor"]||[]).push([[0],{19:function(t,e,a){t.exports=a(43)},42:function(t,e,a){},43:function(t,e,a){"use strict";a.r(e);var n=a(0),r=a.n(n),s=a(17),i=a.n(s),c=a(3),l=a(6),u=a(5),o=a(4),h=a(2),v=a.n(h),p=a(7),d=a(18),b=a.n(d).a.create({baseURL:"http://localhost:4060"});function f(t,e){return m.apply(this,arguments)}function m(){return(m=Object(p.a)(v.a.mark((function t(e,a){var n;return v.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,b.get(a);case 3:n=t.sent,e.setState({status:n.data}),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),e.setState({status:!1});case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))).apply(this,arguments)}function y(t,e){return E.apply(this,arguments)}function E(){return(E=Object(p.a)(v.a.mark((function t(e,a){var n;return v.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,b.get(a,{responseType:"text"});case 3:n=t.sent,e.setState({data:n.data}),t.next=10;break;case 7:return t.prev=7,t.t0=t.catch(0),t.abrupt("return",t.t0.response);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))).apply(this,arguments)}function g(){return(g=Object(p.a)(v.a.mark((function t(e,a,n){return v.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,b.get(a,{responseType:"text",params:{path:n}});case 3:e.setState({validPath:"true"}),t.next=9;break;case 6:t.prev=6,t.t0=t.catch(0),console.log(t.t0.response);case 9:case"end":return t.stop()}}),t,null,[[0,6]])})))).apply(this,arguments)}function P(){return(P=Object(p.a)(v.a.mark((function t(e,a){return v.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,b.get(a);case 3:t.next=8;break;case 5:t.prev=5,t.t0=t.catch(0),console.log(t.t0.response);case 8:case"end":return t.stop()}}),t,null,[[0,5]])})))).apply(this,arguments)}function k(t,e){return j.apply(this,arguments)}function j(){return(j=Object(p.a)(v.a.mark((function t(e,a){var n;return v.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,b.get(a,{responseType:"json"});case 3:n=t.sent,a.includes("virusFoundList")?(e.virusCount=n.data.length,e.setState({virusPathList:n.data})):(e.suspiciousCount=n.data.length,e.setState({suspiciousPathList:n.data})),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),console.log(t.t0);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))).apply(this,arguments)}a(42);var O=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,r=new Array(n),s=0;s<n;s++)r[s]=arguments[s];return(t=e.call.apply(e,[this].concat(r))).state={virusPathList:[],suspiciousPathList:[]},t.virusListGET="/virusFoundList",t.suspiciousListGET="/virusSuspiciousList",t.virusCount=0,t.suspiciousCount=0,t}return Object(l.a)(a,[{key:"componentDidMount",value:function(){var t=this;k(this,this.basePath+this.statusGetPath),setInterval((function(){return k(t,t.props.basePath+t.virusListGET)}),800),setInterval((function(){return k(t,t.props.basePath+t.suspiciousListGET)}),800)}},{key:"listToli",value:function(t){return t.map((function(t){return r.a.createElement("li",{value:""},t)}))}},{key:"render",value:function(){var t=this.listToli(this.state.virusPathList),e=this.listToli(this.state.suspiciousPathList);return r.a.createElement("div",{class:"ui header resultScan"},r.a.createElement("ol",{class:"ui list"},r.a.createElement("div",{class:"ui inverted red segment "},r.a.createElement("li",{value:""},"viruses found: ",this.virusCount)),r.a.createElement("div",{class:"ui secondary red segment"},r.a.createElement("ol",null,t))),r.a.createElement("ol",{class:"ui list"},r.a.createElement("div",{class:"ui inverted yellow segment "},r.a.createElement("li",{value:""},"suspicious viruses found: ",this.suspiciousCount)),r.a.createElement("div",{class:"ui secondary yellow segment"},r.a.createElement("ol",null,e))))}}]),a}(r.a.Component),S=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,s=new Array(n),i=0;i<n;i++)s[i]=arguments[i];return(t=e.call.apply(e,[this].concat(s))).logEnd=r.a.createRef(),t.loggerGetPath="/log",t.state={data:""},t.btnAutoScroll=!0,t.autoScoll=!0,t}return Object(l.a)(a,[{key:"componentDidMount",value:function(){var t=this;y(this,this.props.basePath+this.loggerGetPath),setInterval((function(){return y(t,t.props.basePath+t.loggerGetPath)}),800)}},{key:"componentDidUpdate",value:function(){}},{key:"render",value:function(){var t=this,e=this.state.data,a=this.btnAutoScroll?"disable auto scroll":"enable auto scroll";return r.a.createElement("div",null,r.a.createElement("button",{class:"ui button",onClick:function(){t.btnAutoScroll=!t.btnAutoScroll}}," ",a),r.a.createElement("br",null),r.a.createElement("br",null),r.a.createElement("div",null,r.a.createElement("div",{class:"ui tertiary segment",id:"log"},r.a.createElement("p",{class:"ui black"}," ",e," "),r.a.createElement("div",{class:"log scroll",style:{float:"left",clear:"both"},ref:function(e){t.logEnd=e}}))))}}]),a}(r.a.Component),w=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,r=new Array(n),s=0;s<n;s++)r[s]=arguments[s];return(t=e.call.apply(e,[this].concat(r))).state={status:""},t.scannerType="",t.basePath="",t.statusGetPath="/status",t}return Object(l.a)(a,[{key:"componentDidMount",value:function(){var t=this;f(this,this.basePath+this.statusGetPath),setInterval((function(){return f(t,t.basePath+t.statusGetPath)}),800)}},{key:"isActive",value:function(t){return t?this.activeScan():this.notActiveScan()}},{key:"activeScan",value:function(){return r.a.createElement("div",null,r.a.createElement("h3",{class:"ui large header green"}," Active "),r.a.createElement(S,{basePath:this.basePath}),r.a.createElement(O,{basePath:this.basePath}))}},{key:"notActiveScan",value:function(){return r.a.createElement("div",null,r.a.createElement("h3",{class:"ui large header red"}," Not Active "),r.a.createElement(O,{basePath:this.basePath}))}},{key:"render",value:function(){var t=this.isActive(this.state.status);return r.a.createElement("div",null,r.a.createElement("h3",{class:"ui large dividing header"}," ",this.scannerType," "),t)}}]),a}(r.a.Component),C=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,r=new Array(n),s=0;s<n;s++)r[s]=arguments[s];return(t=e.call.apply(e,[this].concat(r))).initDirectoryGetPath="/initDirectoryPathScan",t.startScanGetPath="/start",t.input="",t.state={validPath:"false",input:"",error:""},t.handleChange=function(e){t.input=e.target.value},t}return Object(l.a)(a,[{key:"handleClick",value:function(){!function(t,e,a){g.apply(this,arguments)}(this,this.props.basePath+this.initDirectoryGetPath,this.input)}},{key:"validInputCheck",value:function(t){return this.state.error.startsWith("invalid")&&"true"!=t?r.a.createElement("div",{class:"ui pointing below label red"},"path not found!"):"false"==t?r.a.createElement("div",{class:"ui pointing below label"},"Enter folder to scan"):r.a.createElement("div",{class:"ui right pointing label green"},"valid path")}},{key:"startScan",value:function(){!function(t,e){P.apply(this,arguments)}(this,this.props.basePath+this.startScanGetPath)}},{key:"render",value:function(){var t=this,e=this.state.validPath;return r.a.createElement("div",null,this.validInputCheck(e),r.a.createElement("div",{class:"ui ".concat("true"==e?"disabled":""," input focus")},r.a.createElement("input",{id:"initDirectoryInput",type:"text",onChange:this.handleChange,placeholder:"init directory"})),r.a.createElement("div",null,r.a.createElement("input",{type:"button",class:"ui green ".concat("true"==e?"disabled":""," button"),onClick:function(){t.handleClick()},value:"submit"})),r.a.createElement("br",null),r.a.createElement("div",null,r.a.createElement("input",{type:"button",class:"ui green ".concat("false"==e?"disabled":""," button"),onClick:function(){t.startScan()},value:"start scan"})))}}]),a}(r.a.Component),x=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,r=new Array(n),s=0;s<n;s++)r[s]=arguments[s];return(t=e.call.apply(e,[this].concat(r))).scannerType="User scan status:",t.basePath="/userScan",t}return Object(l.a)(a,[{key:"notActiveScan",value:function(){return r.a.createElement("div",null,r.a.createElement("div",null,r.a.createElement("h3",{class:"ui large header red"}," Not Active "),r.a.createElement(C,{basePath:this.basePath}),r.a.createElement(O,{basePath:this.basePath})))}}]),a}(w),A=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){var t;Object(c.a)(this,a);for(var n=arguments.length,r=new Array(n),s=0;s<n;s++)r[s]=arguments[s];return(t=e.call.apply(e,[this].concat(r))).scannerType="Schedule scan status:",t.basePath="/scheduleScan",t}return a}(w),G=function(t){Object(u.a)(a,t);var e=Object(o.a)(a);function a(){return Object(c.a)(this,a),e.apply(this,arguments)}return Object(l.a)(a,[{key:"render",value:function(){return r.a.createElement("div",{class:"main"},r.a.createElement("div",{class:"ui inverted segment "},r.a.createElement("div",{class:"ui huge header ",id:"AntiVirusHeader"}," Anti Virus ")),r.a.createElement("div",{class:"ui segment"},r.a.createElement("div",{class:"ui two column very relaxed grid"},r.a.createElement("div",{class:"column"},r.a.createElement(A,null)),r.a.createElement("div",{class:"column"},r.a.createElement(x,null)))))}}]),a}(r.a.Component);i.a.render(r.a.createElement(G,null),document.querySelector("#root"))}},[[19,1,2]]]);
//# sourceMappingURL=main.7aec8c8a.chunk.js.map