import { getDefaultHeader } from "./util";

const request = async (uri, config) => {
  try {
    let res = await fetch(uri, {
      mode: "cors",
      credentials: "include",
      ...config,
    });
    if (res.ok) {
      if (config.method === "DELETE") return;
      res = await res.json();
      return Promise.resolve(res);
    } else {
      return Promise.reject("Error");
    }
  } catch (err) {
    return Promise.reject(err.message);
  }
};

const httpService = {
  get: (uri) => {
    return request(uri, { method: "GET" });
  },
  remove: (uri) => {
    const header = getDefaultHeader();
    return request(uri, { method: "DELETE", headers: header, });
  },
  post: (uri, data, onSuccess) => {
    const header = getDefaultHeader();
    header.append("Content-Type", "application/json");
    return request(
      uri,
      {
        method: "POST",
        headers: header,
        body: JSON.stringify(data),
      },
      onSuccess
    );
  },
  put: (uri, data) => {
    const header = getDefaultHeader();
    header.append("Content-Type", "application/json");
    return request(uri, {
      method: "PUT",
      headers: header,
      body: JSON.stringify(data),
    });
  },
};

export default httpService;
