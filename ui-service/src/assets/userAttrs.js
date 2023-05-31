const email = {
  key: "email",
  label: "E-mail",
  type: "email",
};

const password = {
  key: "password",
  label: "Password",
  type: "text",
};

const roles = [
  {
    value: "DISPATCHER",
    label: "Dispatcher",
  },
  {
    value: "DELIVERER",
    label: "Deliverer",
  },
  {
    value: "CUSTOMER",
    label: "Customer",
  },
];

const role = {
  key: "role",
  label: "Role",
  type: "select",
  options: roles,
};

const rfidToken = {
  key: "rfidToken",
  label: "RFID Token",
  type: "text",
};

export { email, password, role, rfidToken };
