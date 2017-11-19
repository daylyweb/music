
        //检索某元素是否在数组内 在返回index 不在返回-1
        var isIn = function(checked,arrlist){
            for(var index in arrlist)
            {
                if(arrlist[index]===checked){
                    return index;
                }
            }
            return -1;
        }

        var audio,playing=false;
        var shiting = function (m4a,ele){
            if(audio==null){ 
                audio=new Audio();
                audio.src=m4a;
                audio.autoplay=true;
                audio.load();
                audio.loop=true;
            }
            if(!playing && !audio.isplaying){
                audio.play();
                ele.innerText='暂停';
                playing=true;
            } else {
                audio.pause();
                ele.innerText='播放';
                playing=false;
            }
        }

        //将laydate返回的时间范围进行分割 2017-11-9 - 2017-11-10
        var splitTime= function(str){
            if (str==null || str=='' || str==undefined) {
                return new Array('','');
            }
            var arr = str.split(' - ',2);
            var time1 = arr[0].split('-',3);
            var time2 = arr[1].split('-',3);
            arr[0] = new Date(time1[0],time1[1]-1,time1[2],0,0,0).getTime();
            arr[1] = new Date(time2[0],time2[1]-1,time2[2],23,59,59).getTime();
            return arr;
        }

        //存储已打开tab的layid数组 默认首页打开1
        var tabs= new Array('1');

        layui.use(["layer","element","table",'laytpl','laydate','form'],function(){
            var element=layui.element,
	            layer=layui.layer,
	            table = layui.table,
	            $ = layui.jquery,
            	laytpl = layui.laytpl,
            	laydate = layui.laydate,
            	form = layui.form;
            var zantableins,musictableins,commendtableins,feedbacktableins,infotableins;

            var loadzan = function(flag,keyword,start,end){
                if(flag==true && zantableins!=null) zantableins.reload();
                else if(zantableins!=null && keyword!=null || start!=null || end!=null){
                    zantableins.reload({
                        where:{
                            keyword:keyword,
                            start:start,
                            end:end
                        }
                    });
                } else {
                    var options = {
                        elem:"#zantable",
                        cols:[[
                            {checkbox:true},
                            {field: 'id', title: 'ID',width:'5%',align:'center'},
                            {field: 'ip', title: 'IP',width:'15%', align:'center'},
                            {field: 'device', title: '设备',width:'53%', align:'center'},
                            {field: 'time', title: '时间',width:'15%', align:'center',sort:true},
                            {field: 'right', title:'操作',width:'7%',align:'center',toolbar:'#zantoolbar'}
                        ]],
                        page: true,
                        id: 'zan',
                        size:'small',
                        limits:[10,20],
                        limit:10,
                        url:config.zanurl,
                        response:{
                            statusName: 'status',
                            statusCode: 200
                        }
                    };
                    zantableins=table.render(options);
                }
                return zantableins;
            }

            var loadmusic = function(flag,keyword){
                if(flag==true && musictableins!=null) musictableins.reload();
                else if(keyword!=null && musictableins!=null) musictableins.reload({
                    where:{
                        keyword:keyword
                    }
                })
                else{
                    var options =  {
                        elem:"#musictable",
                        cols:[[
                            {checkbox:true},
                            {field: 'songname', title: '歌曲名', minWidth:155,width:'13.3%',align:'center'},
                            {field: 'singer', title: '歌手', minWidth:155,width:'13.3%',align:'center'},
                            {field: 'albumname', title:'专辑名',minWidth:155,width:'13.3%', align:'center'},
                            {field: 'keyword', title:'搜索关键词', minWidth:155,width:'13.3%',align:'center'},
                            {field: 'songid', title: '歌曲id',minWidth:105,width:'11%',align:'center',sort:true},
                            {field: 'songmid', title: '歌曲mid', minWidth:150,width:'12%',align:'center'},
                            {field: 'right', title:'操作',minWidth:200,width:'18%',align:'center', toolbar:'#musictoolbar'}
                        ]],
                        page: true,
                        id: 'music',
                        size:'small',
                        limits:[10,20,30],
                        limit:10,
                        url:config.musicurl,
                        response:{
                            statusName: 'status',
                            statusCode: 200
                        }
                    }
                    musictableins = table.render(options);
                }
                return musictableins;
            }

            var loadcommend = function(flag,keyword){
                if(flag==true && commendtableins!=null) commendtableins.reload();
                else if(keyword!=null && commendtableins!=null) commendtableins.reload({
                    where:{
                        keyword:keyword
                    }
                })
                else{
                    var options = {
                        elem:"#commendtable",
                        cols:[[
                            {checkbox:true},
                            {field: 'songname', title: '歌曲名', minWidth:155,width:'13.5%',align:'center',},
                            {field: 'singer', title: '歌手', minWidth:155,width:'13.5%',align:'center',},
                            {field: 'albumname', title:'专辑名',minWidth:155,width:'13.5%',align:'center'},
                            {field: 'keyword', title:'搜索关键词',minWidth:155,width:'13.5%',align:'center'},
                            {field: 'songid', title: '歌曲id',minWidth:155,width:'13.5%',align:'center',sort:true},
                            {field: 'songmid', title: '歌曲mid', minWidth:155,width:'13.5%',align:'center'},
                            {field: 'right', title:'操作',minWidth:130,width:'13.5%',align:'center', toolbar:'#commendtoolbar'}
                        ]],
                        page: true,
                        id: 'commend',
                        size:'small',
                        limits:[10,20,30],
                        limit:10,
                        url:config.commendurl,
                        response:{
                            statusName: 'status',
                            statusCode: 200
                        }
                    };
                    commendtableins = table.render(options);
                }
                return commendtableins;
            }

            var loadfeedback = function(flag,keyword,start,end){
                if(flag==true && zantableins!=null) feedbacktableins.reload();
                else if(feedbacktableins!=null && keyword!=null || start!=null || end!=null){
                    feedbacktableins.reload({
                        where:{
                            keyword:keyword,
                            start:start,
                            end:end
                        }
                    });
                } else {
                    var options = {
                        elem:"#feedbacktable",
                        cols:[[
                            {checkbox:true},
                            {field: 'id', title: 'ID',minWidth:60,width:'5%',align:'center'},
                            {field: 'name', title: '姓名', minWidth:115,width:'10%',align:'center'},
                            {field: 'concat', title:'联系方式',minWidth:115,width:'10%',align:'center'},
                            {field: 'device', title:'设备型号',minWidth:120,width:'10%',align:'center'},
                            {field: 'content', title: '反馈内容', minWidth:430,width:'38%',align:'center'},
                            {field: 'time', title: '反馈时间', minWidth:170,width:'15%',align:'center'},
                            {field: 'right', title:'操作',minWidth:80,width:'7%',align:'center', toolbar:'#feedbacktoolbar'}
                        ]],
                        page: true,
                        id: 'feedback',
                        size:'small',
                        limits:[10,20,30],
                        limit:10,
                        url:config.feedbackurl,
                        response:{
                            statusName: 'status',
                            statusCode: 200
                        }
                    };
                    feedbacktableins=table.render(options);
                }
                return feedbacktableins;
            }

            var loadinfo = function(flag,keyword,start,end){
                if(flag==true && infotableins!=null) infotableins.reload();
                else if(infotableins!=null && keyword!=null || start!=null || end!=null){
                    infotableins.reload({
                        where:{
                            keyword:keyword,
                            start:start,
                            end:end
                        }
                    });
                } else {
                    var options = {
                        elem:"#infotable",
                        cols:[[
                            {checkbox:true},
                            {field: 'id', title: 'ID',minWidth:60,width:'10%', align:'center'},
                            {field: 'info', title: '信息', minWidth:400,width:'63%',align:'center'},
                            {field: 'time', title: '记录时间', minWidth:170,width:"20%",align:'center'},
                        ]],
                        page: true,
                        id: 'info',
                        size:'small',
                        limits:[10,20,30],
                        limit:10,
                        url:config.infourl,
                        response:{
                            statusName: 'status',
                            statusCode: 200
                        }
                    };
                    infotableins=table.render(options);
                }
                return infotableins;
            }

            //首页tab的关闭按钮添加事件
            var indexclose = document.querySelector(".layui-tab-close");
            $(indexclose).unbind('click');
            indexclose.addEventListener('click',function(){
                layer.confirm('想关闭吗？',{icon: 3, title:'提示',btn:['想','算了']},
                    function(index){
                        layer.msg('不给....',{anim: 6,icon:4});
                    });
                event.stopPropagation();
            },true);

            //添加tab
            var addTab = function(title,content,layid,loadevent,timeFilter){
                //添加tab
                element.tabAdd("tab",{
                    title:title,
                    content:content,
                    id:layid
                });
                //将新增的tab添加进数组
                tabs.push(layid);
                //切换到新增的tab
                element.tabChange("tab",layid);
                if(typeof(loadevent) === 'function') loadevent(false,null);
                if(timeFilter==null) return;
                laydate.render({
                    elem:timeFilter,
                    range: true
                });
                element.init();
            }

            var btnEvent={
                musicQuery:function(){
                    var value = $('input[name=musickeyword]').val();
                    value ? value:'';
                    loadmusic(false,value);
                },
                addMusic:function(){
                    layer.msg('音乐源都是来自QQ音乐，暂不支持自定义添加音乐！',{time:4000});
                },
                setCommendBatch:function(){
                    var checkStatus = table.checkStatus('music');
                    var data = checkStatus.data;
                    if(data==null || data.length<=0){layer.msg('未选中任何数据！');return;}
                    var ids='',length=data.length;
                    for(var index in data)
                    {
                        ids+=(index==length-1) ? data[index].songid+'':data[index].songid+',';
                    }
                    setCommend({
                        url:config.musicurl,
                        commend:'Y',
                        songids:ids,
                        success:function(data){
                            if(data.status==200) loadmusic(true,null);
                            layer.msg(data.msg);
                        },
                        error:function(data){
                            layer.msg('操作失败！');
                        }
                    });
                },
                musicDelBatch:function(){
                    var checkStatus = table.checkStatus('music');
                    var data = checkStatus.data;
                    if(data==null || data.length<=0){layer.msg('未选中任何数据！');return;}
                    var ids='',length=data.length;
                    layer.confirm('确认删除这些数据吗?',function(index){
                        for(var index in data)
                        {
                            ids+=(index==length-1) ? data[index].songid+'':data[index].songid+',';
                        }
                        del({
                            url:config.musicurl,
                            ids:ids,
                            success:function(data){
                                if(data.status==200) loadmusic(true,null);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                },
                musicReload:function(){
                    loadmusic(true,null);
                },
                commendQuery:function(){
                    var value = $('input[name=commendkeyword]').val();
                    value ? value:'';
                    loadcommend(false,value);
                },
                addCommend:function(){
                    layer.msg('请在[所有音乐]中进行推荐音乐的添加',{time:4000});
                    var flag = isIn('3',tabs);
                    //判断tab是否存在 存在切换
                    if (flag!=-1) {
                        element.tabChange("tab",'3');
                    } else { //不存在新增
                        addTab('所有音乐',document.getElementById('musiccontent').innerText,'3',loadmusic,null);
                    }
                },
                unCommendBatch:function(){
                    var checkStatus = table.checkStatus('commend');
                    var data = checkStatus.data;
                    if(data==null || data.length<=0){layer.msg('未选中任何数据！');return;}
                    var ids='',length=data.length;
                    for(var index in data)
                    {
                        ids+=(index==length-1) ? data[index].songid+'':data[index].songid+',';
                    }
                    setCommend({
                        url:config.musicurl,
                        commend:'N',
                        songids:ids,
                        success:function(data){
                            if(data.status==200) loadcommend(true,null);
                            layer.msg(data.msg);
                        },
                        error:function(data){
                            layer.msg('操作失败！');
                        }
                    });
                },
                commendReload:function(){
                    loadcommend(true);
                },
                zanQuery:function(){
                    var value = $('input[name=zankeyword]').val();
                    value ? value:'';
                    var arr = splitTime($('#zanTimeFilter').val());
                    loadzan(false,value,arr[0],arr[1]);
                },
                zanDelBatch:function(){
                    var checkStatus = table.checkStatus('zan');
                    var data = checkStatus.data;
                    if(data==null || data.length<=0){layer.msg('未选中任何数据！');return;}
                    var ids='',length=data.length;
                    layer.confirm('确认删除这些数据吗?',function(index){
                        for(var index in data)
                        {
                            ids+=(index==length-1) ? data[index].id+'':data[index].id+',';
                        }
                        del({
                            url:config.zanurl,
                            ids:ids,
                            success:function(data){
                                if(data.status==200) loadzan(true);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                },
                zanClear:function(){
                    layer.confirm('确认清空吗?',function(index){
                        del({
                            url:config.zanurl,
                            ids:'all',
                            success:function(data){
                                if(data.status==200) loadzan(true);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                },
                zanReload:function(){
                    loadzan(true);
                },
                feedBackQuery:function(){
                    var value = $('input[name=feedbackkeyword]').val();
                    value ? value:'';
                    var arr = splitTime($('#feedBackTimeFilter').val());
                    loadfeedback(false,value,arr[0],arr[1]);
                },
                feedBackDelBatch:function(){
                    var checkStatus = table.checkStatus('feedback');
                    var data = checkStatus.data;
                    if(data==null || data.length<=0){layer.msg('未选中任何数据！');return;}
                    var ids='',length=data.length;
                    layer.confirm('确认删除这些数据吗?',function(index){
                        for(var index in data)
                        {
                            if(index==length-1){ids+=data[index].id+'';break;}
                            ids+=data[index].id+',';
                        }
                        del({
                            url:config.feedbackurl,
                            ids:ids,
                            success:function(data){
                                if(data.status==200) loadfeedback(true);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                },
                feedBackClear:function(){
                    layer.confirm('确认清空吗?',function(index){
                        del({
                            url:config.feedbackurl,
                            ids:'all',
                            success:function(data){
                                if(data.status==200) loadfeedback(true);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                } ,
                feedBackReload:function(){
                    loadfeedback(true);
                },
                infoQuery:function(){
                    var value = $('input[name=infokeyword]').val();
                    value ? value:'';
                    var arr = splitTime($('#infoTimeFilter').val());
                    loadinfo(false,value,arr[0],arr[1]);
                },
                infoClear:function(){
                    layer.confirm('确认清空吗?',function(index){
                        del({
                            url:config.infourl,
                            ids:'all',
                            success:function(data){
                                if(data.status==200) loadinfo(true);
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg("操作失败！");
                            }
                        });
                    });
                },
                infoReload:function(){
                    loadinfo(true);
                },
            }

            //左侧导航条事件监听
            element.on("nav(left)",function(elem){
                var layid = this.getAttribute("lay-id");
                var flag = isIn(layid,tabs);
                //判断tab是否存在 存在切换
                if (flag!=-1) {
                    element.tabChange("tab",layid);
                } else { //不存在新增
                    //根据layid选择添加的内容
                    switch(layid){
                        case '3':
                            addTab('所有音乐',document.getElementById('musiccontent').innerText,layid,loadmusic,null);
                            break;
                        case '4':
                            addTab('音乐推荐',document.getElementById('commendcontent').innerText,layid,loadcommend,null);
                            break;   
                        case '5':
                            addTab('点赞记录',document.getElementById('zancontent').innerText,layid,loadzan,'#zanTimeFilter');
                            break;
                        case '6':
                            addTab('反馈记录',document.getElementById('feedbackcontent').innerText,layid,loadfeedback,'#feedBackTimeFilter');
                            break;
                        case '7':
                            addTab('运行日志',document.getElementById('infocontent').innerText,layid,loadinfo,'#infoTimeFilter');
                            break;
                        default:layer.msg('未定义操作！');
                    }

                    $('.layui-elem-quote .layui-btn').on('click',function(){
                        var event = $(this).data('event');
                        btnEvent[event] ? btnEvent[event].call(this):"";
                    });
                }
            });

            //头部导航事件监听
            element.on('nav(head)', function(elem){
                var layid = this.getAttribute('lay-id');
                var flag = isIn(layid,tabs);
                //判断tab是否存在 存在切换
                if (flag!=-1) {
                    element.tabChange("tab",layid);
                } else {//不存在新增
                    //根据layid选择添加的内容
                    switch(layid){
                        case '8':return;
                        case '9':addTab(elem.text(),document.getElementById('aboutcontent').innerText,layid,null,null);break;
                        case '10':addTab(elem.text(),document.getElementById('changepasscontent').innerText,layid,null,null);break;
                        default:return;//layer.msg('未定义操作！');
                    }
                    
                }
            });

            //tab选项卡删除事件
            element.on('tabDelete(tab)', function(data){
                //this 当前Tab标题所在的原始DOM元素
                //data.index   得到当前Tab的所在下标
                //data.elem 得到当前的Tab大容器
                var parent = this.parentNode;
                var id = parent.getAttribute("lay-id");
                var index = isIn(id,tabs);
                tabs.splice(index,1);
            });


            /*  发送推荐请求
                {
                    url:请求的url,
                    songids:设置推荐状态的所有id,    格式 1,2,3 或 all 全部
                    commend:Y or N, 设置歌曲的推荐状态
                    succes:请求成功回调,
                    error:请求错误回调
                }
    
            */
            var setCommend = function(obj){
                $.ajax(obj.url,{
                    type:'post',
                    dataType:'json',
                    data:{
                        _method:'put',
                        songids:obj.songids,
                        commend:obj.commend
                    },
                    success:function(data){
                        obj.success.call(this,data);
                    },
                    error:function(data){
                        obj.error.call(this,data);
                    }
                });
            }

            /*  发送删除请求  
                {
                    url:请求的url,
                    ids:删除的所有id,  格式 1,2,3 或 all 全部
                    succes:请求成功回调,
                    error:请求错误回调
                }
            */
            var del = function(obj){
                $.ajax(obj.url,{
                    type:'post',
                    dataType:'json',
                    data:{
                        _method:'delete',
                        ids:obj.ids
                    },
                    success:function(data){
                        obj.success.call(this,data);
                    },
                    error:function(data){
                        obj.error.call(this,data);
                    }
                });
            }

            //音乐列表表格
            table.on('tool(music)',function(obj){
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值
                var tr = obj.tr; //获得当前行 tr 的DOM对象
                if(layEvent==="setCommend"){
                    setCommend({
                        url:config.musicurl,
                        commend:'Y',
                        songids:data.songid,
                        success:function(data){
                            if(data.status==200){
                                tr.last().find("#changebtn").attr('lay-event','unCommend').text('取推');
                            }
                            layer.msg(data.msg);
                        },
                        error:function(data){
                            layer.msg('操作失败！');
                        }
                    });
                } else if(layEvent==="unCommend"){
                    setCommend({
                        url:config.musicurl,
                        commend:'N',
                        songids:data.songid,
                        success:function(data){
                            if(data.status==200){
                                tr.last().find("#changebtn").attr('lay-event','setCommend').text('推荐');
                            }
                            layer.msg(data.msg);
                        },
                        error:function(data){
                            layer.msg('操作失败！');
                        }
                    });
                } else if(layEvent==="del"){
                    layer.confirm('确认删除吗?',function(index){
                        del({
                            url:config.musicurl,
                            ids:data.songid,
                            success:function(data){
                                if(data.status==200){
                                    obj.del();
                                }
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg('操作失败！');
                            }
                        });
                    });
                } else if(layEvent==="view"){
                    view(data);
                } else {
                    layer.msg('未知操作！');
                }
                
            });

            //查看歌曲信息
            var view = function(data){
                laytpl(document.getElementById('musicinfo').innerText).render(data,function(str){
                    layer.open({
                        type:1,
                        title:'音乐信息',
                        content:str,
                        resize:false,
                        id:10,
                        scrollbar:false,
                        maxWidth:'250px',
                        maxHeight:'460px',
                        shade:0.8,
                        cancel: function(index, layero){ 
                            if(audio!=null){
                                audio.pause();
                                audio=null;
                                playing=false;
                            }
                            layer.close(index)
                        }
                    });
                });
            }

            //推荐列表表格
            table.on('tool(commend)',function(obj){
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值
                if(layEvent==="view"){
                    view(data);
                } else if(layEvent==="unCommend"){
                    setCommend({
                        url:config.musicurl,
                        commend:'N',
                        songids:data.songid,
                        success:function(data){
                            if(data.status==200){
                                    obj.del();
                            }
                            layer.msg(data.msg);
                        },
                        error:function(data){
                            layer.msg('操作失败！');
                        }
                    });
                } else {
                    layer.msg('未知操作！');
                }
            });

            //点赞记录表格
            table.on('tool(zan)',function(obj){
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值
                if(layEvent==="del"){
                    layer.confirm('确认删除吗?',function(index){
                        del({
                            url:config.zanurl,
                            ids:data.id,
                            success:function(data){
                                if(data.status==200){
                                    obj.del();
                                }
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg('操作失败！');
                            }
                        });
                    });
                } else {
                    layer.msg('未知操作！');
                }
            });

            //反馈记录表格
            table.on('tool(feedback)',function(obj){
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值
                if(layEvent==="del"){
                    layer.confirm('确认删除吗?',function(index){
                        del({
                            url:config.feedbackurl,
                            ids:data.id,
                            success:function(data){
                                if(data.status==200){
                                    obj.del();
                                }
                                layer.msg(data.msg);
                            },
                            error:function(data){
                                layer.msg('操作失败！');
                            }
                        });
                    });
                } else {
                    layer.msg('未知操作！');
                }
            });
            
            //提交密码表单监听
            form.on('submit(changepass)',function(data){
            	$.ajax(config.userurl,{
                    type:'post',
                    dataType:'json',
                    data:{
                        _method:'put',
                        oldpass:data.field.oldpass,
                        newpass1:data.field.newpass1,
                        newpass2:data.field.newpass2
                    },
                    success:function(data){
						if(data.status==200){
							layer.msg(data.msg+" 3秒后将退出登录！届时需重新登录");
							setTimeout(function(){window.location.href=config.outurl},3000);
						} else {
							layer.msg(data.msg);
						}
                    },
                    error:function(data){
                        layer.msg("操作失败！请稍候重试！");
                    }
                });
            	return false;
            });
            
            form.verify({
            	twoConfirm:function(value,item){
            		let name = item.getAttribute("name");
            		if(name= "newpass2"){
        				if($('input[name=newpass1]').val()!=value){
        					return "两次输入的密码不一致";
        				}
            		}
            	}
            });
        });

        window.onload=function(){
           var left =  document.getElementById('openleft');
           var flag=false;
           var slide = document.querySelector('.layui-side-tik');

           var closeSlide = function(btn,slide){
                slide.style.animation="closeleft 1s forwards";
                btn.style.animation="openleftbtn 1.5s forwards";
           }

           var openSlide = function(btn,slide){
                slide.style.animation="openleft 1s forwards";
                btn.style.animation="closeleftbtn 1.5s forwards";
           }

           left.onclick=function(){
                event.stopPropagation();
                if(!flag){
                    openSlide(this,slide);
                    flag=!flag;
                } else{
                    closeSlide(this,slide);
                    flag=!flag;
                }
           }

           document.onclick=function(){
                if(flag){
                    closeSlide(left,slide);
                    flag=!flag;
                }
           }
        }