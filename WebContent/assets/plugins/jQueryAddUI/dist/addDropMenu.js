var GUID=function(){function e(){do var t="xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var t=16*Math.random()|0,r="x"==e?t:3&t|8;return r.toString(16)});while(!e.register(t));return t}return e.create=function(){return e()},e.version="1.2.0",e.list=[],e.exists=function(t){return e.list.indexOf(t)>-1},e.register=function(t){return e.exists(t)?!1:(e.list.push(t),!0)},e}(),Obj=function(){function toFunc(e){if("function"==typeof e)return e;if("string"==typeof e){if(void 0!=window[e]&&"function"==typeof window[e])return window[e];try{return new Function(e)}catch(t){}}return function(){return e}}function Obj(){this._guid=GUID(),Object.defineProperty(this,"guid",{get:function(){return this._guid},set:function(){}}),this._handlers=[],this.on=function(e,t,r){for(var i="all",n=null,s=0,a=0;a<arguments.length;a++)"string"==typeof arguments[a]?i=arguments[a].toLowerCase().split(" "):arguments[a]instanceof Array?i=$.map(arguments[a],function(e,t){return e.toLowerCase()}):"function"==typeof arguments[a]?n=arguments[a]:"number"==typeof arguments[a]&&(s=arguments[a]);if(!n)return this;for(var a=0;a<i.length;a++)this._handlers.push({event:i[a],handler:n,max_count:s,trigger_count:0});return this},this.off=function(e,t){if(void 0===t&&"function"==typeof e)for(var t=e,r=0;r<this._handlers.length;r++)this._handlers[r].handler==t&&this._handlers.splice(r--,1);else if(void 0===t&&"string"==typeof e){e=e.toLowerCase().split(" ");for(var r=0;r<this._handlers.length;r++)e.indexOf(this._handlers[r].event)>-1&&this._handlers.splice(r--,1)}else{e=e.toLowerCase().split(" ");for(var r=0;r<this._handlers.length;r++)e.indexOf(this._handlers[r].event)>-1&&this._handlers[r].handler==t&&this._handlers.splice(r--,1)}return this},this.trigger=function(e,t){e=e.toLowerCase().split(" ");for(var r=0;r<this._handlers.length;r++)(e.indexOf(this._handlers[r].event)>-1||"all"==this._handlers[r].event)&&toFunc(this._handlers[r].handler).call(this,"all"!=this._handlers[r].event?this._handlers[r].event:e.join(" "),t);return this},this._elements=$(),this.renderer=function(){var e=$("<div class='Obj'></div>");for(var t in this)0==t.indexOf("_")&&"function"!=typeof this[t]&&-1==["_handlers","_elements","_guid"].indexOf(t)&&e.append("<div class='Obj-member'><div class='Obj-member-key'>"+t.substr(1)+"</div><div class='Obj-member-value'>"+this[t]+"</div></div>");return e},this.refresher=function(e){return this.renderer.apply(this)},this.destroyer=function(e){},this.render=function(e,t){var r=this;if(void 0===e)var e="body";if(void 0===t)var t="append";else t=t.toLowerCase();var i=[].slice.call(arguments,2),n=this;return $(e).each(function(e,s){s=$(s);var a=$(r.renderer.apply(r,i));a.attr("guid",r.guid),r._elements=r._elements.add(a),"append"==t?s.append(a):"prepend"==t?s.prepend(a):"after"==t?s.after(a):"before"==t?s.before(a):"return"==t?n=a:"replace"==t&&(s.after(a),s.remove())}),this.trigger("render"),n},this.refresh=function(e){for(var t=$(),r=0;r<this._elements.length;r++){var i=this._elements.eq(r),n=this.refresher.call(this,i,e);n?(n.attr("guid",this.guid),this._elements=this._elements.not(i),i.after(n),i.remove(),t=t.add(n)):t=t.add(i)}return this._elements=t,this},this.destroy=function(){var e=this;return this._elements.each(function(t,r){var i=$(r);i.off(),i.find("*").off(),e.destroyer.call(e,i)}),this._elements.remove(),this._elements=$(),delete Obj.directory[this.guid],this},this.defMember=function(e,t,r,i){for(var n=this,s=["handlers","on","off","trigger","elements","render","renderer","refresh","refresher","destroy","destroyer","defMember","defSettings","defMethod","guid"],a=0;s>a;a++)if(s[a]==e||"_"+s[a]==e)return!1;this["_"+e]=void 0===t?null:t,Object.defineProperty(this,e,{get:function(){var t=this["_"+e];return i&&(t=i.call(n,t)),this.trigger("get"+e+" "+e,t),t},set:function(t){if(r){var i=r.call(n,t);void 0!==i&&(t=i)}this["_"+e]=t,this.trigger("set"+e+" "+e,t),this.refresh(e)}})},this.defSettings=function(e){if(void 0===e)var e={};this._settings=e,Object.defineProperty(this,"settings",{get:function(){return this.trigger("getsettings settings",this._settings),this._settings},set:function(e){this._settings=$.extend(this._settings,e),this.trigger("setsettings settings",this._settings),this.refresh("settings")}})},this.defMethod=function(e,t){var r=this;this["_"+e]=t,this[e]=function(){var t=r["_"+e].apply(r,arguments);return r.trigger(e,arguments),void 0!=t?t:r}},Obj.directory[this.guid]=this}return Obj.version="2.1.1",Obj.directory={},Obj.extend=function(e,t){t||(t=Obj);var r=function(){t.apply(this,arguments),e.apply(this,arguments)};return e.prototypoe=Object.create(t.prototype),r.prototype=Object.create(e.prototype),r},Obj.create=function(o){function Proto(){Obj.apply(this)}if("function"==typeof o)return Obj.extend(o);if("object"==typeof o){var cc="function Proto(){Obj.apply(this);";for(var k in o){var v=o[k];"function"==typeof v?cc+=["init","renderer","refresher","destroyer"].indexOf(k)>-1?"this."+k+"="+v+";":"this.defMethod('"+k+"',"+v+");":("string"==typeof v&&(v='"'+v+'"'),cc+="this.defMember('"+k+"', "+v+");")}return o.init&&(cc+="this.init.apply(this,arguments);"),cc+="};Proto.prototype = Object.create(Obj.prototype);",eval(cc),Proto}return Proto.prototype=Object.create(Obj.prototype),Proto},Obj.get=function(e){return Obj.directory[e]},Obj["delete"]=function(e){return"object"==typeof e?e.destroy():Obj.get(e).destroy(),Obj},Obj}();!function(e){if("undefined"==typeof t)var t={version:{},auto:{disabled:!1}};t.version.DropMenu="1.0.0",t.DropMenu=function(i,n){var r=e(i).map(function(i,r){var s=e(r),a=e.extend({},s.data(),n),o=new t.DropMenu.obj(a.label,s.children(),a);return o.render(s,"replace"),o});return 0==r.length?null:1==r.length?r[0]:r},t.DropMenu.obj=Obj.create(function(t,i,n){this.defSettings({pin:"top-left",cover:!0,width:150}),this.defMember("label",'<i class="material-icons">&#xE5C5;</i>'),this.defMember("$items",e()),this.renderer=function(){var t=e("<div class='addui-dropMenu'></div>"),i=e("<div class='addui-dropMenu-btn'>"+this._label+"</div>").appendTo(t),n=e("<div class='addui-dropMenu-menu'></div>").appendTo(t);return n.css("width",this._settings.width),this._$items.addClass("addui-dropMenu-item").appendTo(n),i.on("click",function(t){var i=n.hasClass("addui-dropMenu-open");n.toggleClass("addui-dropMenu-open"),i||setTimeout(function(){e(window).one("click",function(){n.removeClass("addui-dropMenu-open")})},301)}),t.addClass("addui-dropMenu-pin-"+this._settings.pin).addClass("addui-dropMenu-cover-"+this._settings.cover),t},this.init=function(e,t,i){e&&(this.label=e),t&&(this.$items=t),i&&(this.settings=i)},this.init.apply(this,arguments)}),e.fn.addDropMenu=function(e){t.DropMenu(this,e)},t.auto.DropMenu=function(){t.auto.disabled||e("[data-addui=DropMenu], [data-addui=dropMenu], [data-addui=dropmenu]").addDropMenu()},e(function(){t.auto.DropMenu()})}(jQuery);
