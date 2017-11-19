
			var audio,
				row=0,
				result=0,
				isplaying=false,
				canplay=false,
				iszan=false,
				audio=null,
				count=0,
				fankuiindex;
			var searchresult = new Array(),
				playlist = new Array(),
				myfav=new Array();
			var str = $('html').css('font-size');
			var rem = str.substr(0,str.indexOf('px'));
			var obj1 = document.getElementById('slide');
			var Ystr = obj1.style.transform || obj1.style.mozTransform || obj1.style.webkitTransform || 'translateY(9rem)';
			var Y  = Ystr.substr(Ystr.indexOf('(')+1,Ystr.indexOf('rem')-Ystr.indexOf('(')-1);
			/*
			歌单数组格式
			playlist = [
				{
					albumimg,:"https://y.gtimg.cn/music/photo_new/T002R500x500M000003iNSPm34eEnI.jpg",
					albumname:"白马照青衣",
					commend:"N",
					infourl:"https://c.y.qq.com/v8/playsong.html?songmid=000s7vR207WSYs",
					keyword:"白马照青衣",
					lrc:"http://music.qq.com/miniportal/static/lyric/55/202773255.xml",
					m4aurl:"http://ws.stream.qqmusic.qq.com/202773255.m4a?fromtag=46",
					singer:"绯村柯北",
					songid:202773255,
					songmid:"000s7vR207WSYs",
					songname:"白马照青衣"
				},
			];*/

			//音频开始播放
			var play = function(){
				$('#playbtn').removeClass('icon-bofang').addClass('icon-pause');
				isplaying=true;
			}

			//音频被暂停
			var pause = function(){
				$('#playbtn').removeClass('icon-pause').addClass('icon-bofang');
				isplaying=false;
			}
			
			//歌词追加到页面
			var appendlrc = function(data){
				$('#slide').empty();
				if(data.status==200){
					$('#slide').css('transform','translateY('+Y+'rem)');
					var odata = data.data;
					for (var i = 0 in odata) {
						if(odata[i].lrc.match(/\S.*/)){
							$('<p>').text(odata[i].lrc).data('time',odata[i].time).appendTo('#slide');
						}
					}
					return;
				}else{
					$('#slide').css('transform','translateY(9rem)');
					$('<p>').text(data.msg).addClass('now').appendTo('#slide');
					return;
				}
				$('<p>').text('歌词加载失败').addClass('now').appendTo('#slide');
			}
			
			//歌词api
			var getlrc=function(songid,songmid){
				var url = 'lrc/'+songid+'/'+songmid;
				$.getJSON(url,function(data){
					if(data!=null)	appendlrc(data);
					else msg('歌词加载失败');
				});
			}
			
			//歌单页加载收藏歌曲
			var loadmyfav = function(myfav){
				$('#playlist>ul').empty();
				for(var a in myfav){
					var li = $('<li>');
					$("<span>").text(myfav[a].singer+' - '+myfav[a].songname).appendTo(li);
					$("<span class='iconfont icon-yichu'>").appendTo(li);
					$("<span class='iconfont icon-jiarugedan isfav'>").appendTo(li);
					li.appendTo('#playlist>ul');
				}
			}

			//搜索页加载搜索结果
			var loadresult = function(result){
				$('#search ul').empty();
				for(var i=0 in result){
					$('<li>').text(result[i].singer+' - '+result[i].songname).appendTo('#search ul');
				}
				lievent();
			}
			
			//加载playlist里的第i首音乐
			var loadMusic  = function(i){
				var item=playlist[i];
				if(audio==null){
					audio = new Audio();
					audio.addEventListener('ended',next, false);
					audio.addEventListener("timeupdate",timeupdate,false);
					audio.addEventListener("play",play,false);
					audio.addEventListener("pause",pause,false);
				}
				audio.src="http://ws.stream.qqmusic.qq.com/"+item.songid+".m4a?fromtag=46";
				audio.autoplay=true;
				audio.load();
				$('#albumimg').attr('src',"https://y.gtimg.cn/music/photo_new/T002R500x500M000"+item.albummid+".jpg");
				$('#tag strong').text(item.songname);
				$('.artist').text(item.singer);
				$('.album').text(item.albumname);
				getlrc(item.songid,item.songmid);
				row=0;result=0;
			}

			//tab切换 播放器
			$('.icon-yinle').on('click',function(){
				if($(this).hasClass('selected')) return;
				$('header').css('display','block');
				$('.search-top').css('display','none');
				$('#player').css('display','block');
				$('#search').css('display','none');
				$('#playlist').css('display','none');
				$('.selected').removeClass('selected');
				$('header strong').text('音乐播放');
				if(iszan){
					$('header>span:last').removeClass('icon-tishi').addClass('icon-dianzan').addClass('iszan');
				}
				else{
					$('header>span:last').removeClass('icon-tishi').removeClass('iszan').addClass('icon-dianzan');
				}
				$(this).addClass('selected');
			});

			//tab切换 搜索
			$('.icon-sousuo').on('click',function(){
				if($(this).hasClass('selected')) return;
				$('.search-top').css('display','block');
				$('header').css('display','none');
				$('#player').css('display','none');
				$('#search').css('display','block');
				$('#playlist').css('display','none');
				$('.selected').removeClass('selected');
				$(this).addClass('selected');
			});

			//tab切换 歌单
			$('.icon-gedan').on('click',function(){
				if($(this).hasClass('selected')) return;
				$('header').css('display','block');
				$('.search-top').css('display','none');
				$('#player').css('display','none');
				$('#search').css('display','none');
				$('#playlist').css('display','block');
				$('.selected').removeClass('selected');
				$('header strong').text('我的歌单');
				if(iszan){
					$('header>span:last').removeClass('icon-dianzan').removeClass('iszan').addClass('icon-tishi');
				}
				else{
					$('header>span:last').removeClass('icon-dianzan').addClass('icon-tishi');
				}
				$(this).addClass('selected');
			});

			//提交反馈
			var feedback=function(){
				var name = $('input[name=name]').val(),
					concat = $('input[name=concat]').val(),
				    device = $('input[name=device]').val(),
				    content = $('textarea[name=content]').val() || $('textarea[name=content]').text();
				    if(name=="" || concat=="" ||device=="" || content==""){
				    	msg('所有输入框不能留空！');
				    }else{
				    	$.post('feedback',{
				    		'name':name,
				    		'concat':concat,
				    		'device':device,
				    		'content':content
				    	},function(data){
				    		if(data.status==200){
				    			msg(data.msg);
				    			setTimeout(layer.close(fankuiindex),2000);
				    		}else{
				    			msg(data.msg);
				    		}
				    	},"json");
				    }
			}

			//弹出提示
			var msg = function(contentstr){
				var index = layer.open({
					content:contentstr,
					skin:'msg',
					time:3
				});
				//setTimeout(layer.close(index),4000);
			}

			//反馈按钮事件
			$('.icon-fankui').on('click',function(){
				var content='<h1 class="title">建议反馈</h1>';
						content+='<form>';
							content+='&nbsp;&nbsp;&nbsp;&nbsp;姓名: &nbsp; &nbsp; &nbsp;<input type="text" name="name"/><br>';
							content+='联系方式：<input type="text" name="concat"/><br>';
							content+='设备型号：<input type="text" name="device" />';
							content+='<div class="fankui">反馈/建议：<br>';
								content+='<textarea name="content"></textarea><br>';
								content+='<input type="button" class="btn" value="提交" onclick="feedback();" />';
								content+='<input type="button" class="btn" value="关闭" onClick="layer.closeAll();" />';
							content+='</div>';	
						content+='</form>';

				fankuiindex = layer.open({
					type:1,
					className:'tips',
					shadeClose:false,
					content:content
				});
				/*$('.btn:eq(0)').click(feedback(fankui));*/
			});

			//右上按钮事件
			$('#right').on('click',function(){
				var obj=$(this);
				if(obj.hasClass('icon-dianzan')){
					var content;
					if(iszan){
						content='<h1 class="title">3Q</h1><p>赞已经够多辣！</p>';
						layer.open({
							type:1,
							className:'tips',
							content:content
						});
					}
					else{
						$.post('zan',{},function(data){
							if(data.status==200){
								content='<h1 class="title">3Q</h1><p>'+data.msg+'</p>';
								iszan=true;
								obj.addClass('iszan');
							}else{
								content='<h1 class="title">3Q</h1><p>'+data.msg+'</p>';
								iszan=false;
							}
							layer.open({
								type:1,
								className:'tips',
								content:content
							});
						},"json");
					}
				}
				else if(obj.hasClass('icon-tishi')){
					layer.open({
						type:1,
						className:'tips',
						content:'<h1 class="title">Tips</h1><p>欢迎使用小只音乐播放器</p><p style="text-align:left">在使用时您该注意以下问题：</p><ul><li>&nbsp;&nbsp;&nbsp;&nbsp;1、使用时请关闭浏览器的无痕模式，否则您无法保存喜欢的音乐到本地。</li><li>&nbsp;&nbsp;&nbsp;&nbsp;2、清除浏览器数据后收藏的音乐将消失，请注意备份歌单</li></ul>'
					});
				}
				else{
					return;
				}
			});
			
			//下一首按钮点击
			$('.icon-xiayishou').on('click',next);

			//上一首按钮点击
			$('.icon-shangyishou').on('click',last);

			//暂停按钮
			$('#playbtn').on('click',function(){
				if(isplaying){
					audio.pause();
				}
				else{
					audio.play();
				}

			});

			//下一首
			function next(){
				audio.pause();
				count++;row=0;
				if(count>=playlist.length){count=0;}
				loadMusic(count);
				playingstatus(count);
			}

			//上一首
			function last(){
				audio.pause();
				count--;row=0;
				if(count<0){count=playlist.length-1;}
				loadMusic(count);
				playingstatus(count);
			}

			//歌词向上滚动
			var lrcslide = function(i){
				var obj = document.getElementById('slide');
				obj.style.transform='translateY('+(Y-i*3)+'rem)';
				obj.style.transition='transform 1s';
			}

			//播放进度更新
			function timeupdate(){
				var obj = $('#slide>p:eq('+row+')');
				var height = obj.height();
				var cc = Math.floor(height/(3*rem));
	        	if(this.currentTime>=obj.data('time'))
	         	{
	        		if(cc>1){
	        			result+=cc-1;
	        		}
	         		$('p.now').removeClass('now');
	         		obj.addClass('now');
	           		lrcslide(result+row);
	           	 	row++;
	       		}
			}
			
			//音乐搜索
			var MusicSearch = function(){
				var keyword = this.firstElementChild.value || $("input[type=search]").val();
				if(keyword =='' || null){
					msg('请输入需要搜索的内容！');
					return;
				}
				var sessionresult=sessionStorage.getItem(keyword);
				//本地有搜索数据直接使用
				if(sessionresult!=null){
					searchresult = JSON.parse(sessionresult);
					loadresult(searchresult);
				} else { //本地无搜索数据调用api搜索
					var index = layer.open({
						type:2,
						content:'Seraching....',
						shadeClose:false
					});
					$.ajax({
						type:'get',
						url:"search/"+encodeURI(keyword),
						dataType:'json',
						/*timeout:10000,*/
						success:function(data){
							layer.close(index);
							if(data.status==200 && data.data!=null){
								searchresult = data.data;
								loadresult(searchresult);
								sessionStorage.setItem(keyword,JSON.stringify(searchresult));
							}else{
								msg(data.msg);
							}
						},
						error:function(){
							layer.close(index);
							msg('搜索失败！未知错误！请联系我们！');
						}
					});
				}
			}

			//搜索按钮点击
			$("#searchbtn").on('click',MusicSearch);
			$(".search-group").bind("search",function(){
				MusicSearch.call(this);
			});
			
			//搜索结果页点击播放
			var lievent = function(){
				$('#search ul li').each(function(i){
					$(this)[0].onclick=function(){
						if($(this).hasClass('playing')){reurn;}
						var length = playlist.push(searchresult[i]);
						loadMusic(length-1);
						$('.playing').removeClass('playing');
						$(this).addClass('playing');
						var li = $('<li>');
						$("<span>").text(searchresult[i].singer+' - '+searchresult[i].songname).addClass('playing').appendTo(li);
						$("<span class='iconfont icon-yichu'>").appendTo(li);
						$("<span class='iconfont icon-jiarugedan'>").appendTo(li);
						li.appendTo('#playlist>ul');
						spanevent();
					}
				});
			}

			//下一首时播放列表切换播放歌曲列表
			var playingstatus = function(n){
				$('.playing').removeClass('playing');
				$('#playlist ul li:eq('+n+') span:first-child').addClass('playing');
			}

			//歌单列各个按钮事件绑定
			var spanevent = function(){
				//点击播放
				$('#playlist ul li span:first-child').each(function(i){
					$(this)[0].onclick=function(){
						loadMusic(i);
						$('#playlist ul li span.playing').removeClass('playing');
						$(this).addClass('playing');
					}
				});

				//加入收藏
				$('span.icon-jiarugedan').each(function(i){
					var obj = $(this);
					obj[0].onclick=function(){
						var index = $.inArray(playlist[i],myfav);
						var ol=myfav.length;
						if(obj.hasClass('isfav')){//移除收藏
							if(index!=-1)
							{
								if(myfav.splice(index,1)[0]==playlist[i])
								{
									localStorage.setItem('myfav',JSON.stringify(myfav));
									obj.removeClass('isfav');
									msg('移除成功');
								}
							}
						} else{//添加收藏
							if(index==-1)
							{
								var nl = myfav.push(playlist[i]);
								if(nl>ol)
								{
									localStorage.setItem('myfav',JSON.stringify(myfav));
									obj.addClass('isfav');
									msg('收藏成功');
								}
							}
						}
					}
				});

				//移除按钮
				$('.icon-yichu').each(function(i){
					var obj=$(this);
					obj[0].onclick=function(){
						layer.open({
						    content: '您确定要从歌单中移除这首歌曲吗？',
						    shadeClose:false,
						    btn: ['是的', '雅蠛蝶'],
						    yes: function(index){
						    		var ol=playlist.length;
									playlist.splice(i,1);
									var nl=playlist.length;
									if(ol-nl==1){
									 	obj.parent().remove();
									    spanevent();
									    layer.close(index);
									}
						    }
						});
					}
				});
			}

			//自适应分辨率
			var setfontsize = function(){
				var height=$(this).height();
				var width=$(this).width();
				if(height>390 && width>670){
					$('html').css('font-size',((height*width*window.devicePixelRatio)/28353)/window.devicePixelRatio+"px");
				}else{
					$('html').css('font-size',((height*width*window.devicePixelRatio)/13475)/window.devicePixelRatio+"px");
				}	
			}

			//窗口大小改变自适应分辨率
			$(window).resize(setfontsize);
			
		$(function(){
			setfontsize();
			//页面加载时获取推荐歌曲
			var MusicCommend=sessionStorage.MusicCommend;
			if(MusicCommend!=null){
				searchresult=JSON.parse(MusicCommend);
				loadresult(searchresult);
			} else{
				$.getJSON("commend",function(data){
					if(data.status==200){
						searchresult=data.data;
						sessionStorage.setItem('MusicCommend',JSON.stringify(searchresult));
						loadresult(searchresult);
					} else msg(data.msg);
				});
			}
			
			//获取本地收藏歌曲作为歌单
			if(localStorage.myfav!=null){
				myfav = JSON.parse(localStorage.myfav);
				if(myfav.length<1){return;}
				playlist=myfav.concat(); //数组=对象引用 用concat复制新数组
				loadmyfav(myfav);
				spanevent();
				loadMusic(count);
			}
		});