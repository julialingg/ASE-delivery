function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(";");
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i].trim();
    if (c.indexOf(name) === 0) return c.substring(name.length, c.length);
  }
  return "";
}

function getDefaultHeader() {
  const header = new Headers();
  const CSRF_TOKEN = getCookie("XSRF-TOKEN");
  if (CSRF_TOKEN) {
    header.append("X-XSRF-TOKEN", CSRF_TOKEN);
  }
  return header;
}

export { getCookie, getDefaultHeader };
