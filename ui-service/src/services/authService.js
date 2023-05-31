import { getCookie } from "./util";
import { Jose } from 'jose-jwe-jws';
const serviceURL = "http://localhost:8081/auth";

const authService = {

  login: async ({ username, password }) => {
    let header = new Headers();

    try {
      await fetch(`${serviceURL}/csrf`, {
        credentials: "include",
      });

      header.append("X-XSRF-TOKEN", getCookie("XSRF-TOKEN"));

      let key = await fetch(`${serviceURL}/pkey`).then(res => res.json());
      let rsaKey = await Jose.Utils.importRsaPublicKey({
        "e": parseInt(key.e),
        "n": key.n
      }, "RSA-OAEP");

      let cryptographer = new Jose.WebCryptographer();
      let encrypter = new Jose.JoseJWE.Encrypter(cryptographer, rsaKey);
      let pw = await encrypter.encrypt(JSON.stringify({ password })).then(result => result)
      
      header.append("Content-Type", "application/json");

      let res = await fetch(`${serviceURL}/jwe`, {
        method: "POST",
        headers: header,
        credentials: "include",
        body: JSON.stringify({ username, password: pw }),
      });

      const ok = res.ok;
      res = await res.text();
      if (ok) {
        localStorage.setItem("jwtToken", res);
      } else {
        return Promise.reject(res);
      }
    } catch (err) {
      console.log("error", err);
      return Promise.reject(err.message);
    }
  },

  // logout: () => fetch(`${serviceURL}`, { method: "DELETE", credentials: "include" }),
  logout: () => {
    localStorage.removeItem("jwtToken");
  },

  getUser: () => {
    const jwtToken = localStorage.getItem("jwtToken");
    if (jwtToken) {
      const token = jwtToken.split(".");
      const payload_token = token[1]; // get payload
      const userJson = JSON.parse(
        decodeURIComponent(window.atob(payload_token))
      );

      if (userJson.exp > Date.now()) {
        localStorage.removeItem("jwtToken");
        return {};
      }
      return {
        username: userJson.sub,
        role: userJson.roles[0].role,
      };
    }
    return {};
  },
};

export default authService;
