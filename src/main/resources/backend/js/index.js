/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数,获取id参数值
function requestUrlParam(argname){
  var url = location.href   //获取完整的请求的url
  var arrStr = url.substring(url.indexOf("?")+1).split("&")  //indexOf 方法返回一个整数值，指出 String 对象内子字符串的开始位置。
  for(var i =0;i<arrStr.length;i++)
  {
      var loc = arrStr[i].indexOf(argname+"=")
      if(loc!=-1){                            //很明显这个取得是第一个
          return arrStr[i].replace(argname+"=","").replace("?","")  //获取id参数值
      }
  }
  return ""
}
