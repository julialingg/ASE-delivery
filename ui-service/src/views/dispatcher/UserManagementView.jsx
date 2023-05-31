import React, { useRef, useState } from "react";
import { Button, TextField } from "@mui/material";
import ManagementTable from "../../components/ManagementTable";
import Select from "../../components/form/Select";
import userService from "../../services/userService";
import { email, password, role, rfidToken } from "../../assets/userAttrs";

export default function UserManagementView() {
  const tableRef = useRef(null);

  const FormMixin = () => {
    const selectedRow = tableRef.current.selectedRow;
    const { data: defaultValues, index } = selectedRow;
    const defaultRfidToken = defaultValues[rfidToken.key];
    const defaultRole = defaultValues[role.key];
    const [selectedRole, setSelectedRole] = useState(
      defaultRole ? defaultRole : ""
    );
    const [newPassword, setNewPassword] = useState(false);
    const handleChange = (value) => setSelectedRole(value);
    return (
      <>
        <Select
          label={role.label}
          name={role.key}
          defaultValue={defaultRole}
          options={role.options}
          onChange={handleChange}
        />
        {selectedRole && ["DELIVERER", "CUSTOMER"].includes(selectedRole) && (
          <TextField
            name={rfidToken.key}
            label={rfidToken.label}
            defaultValue={defaultRfidToken ? defaultRfidToken : ""}
            required
            fullWidth
            variant="filled"
          />
        )}
        {index > -1 && !newPassword && (
          <Button variant="outlined" onClick={() => setNewPassword(true)}>
            New Password
          </Button>
        )}
        {newPassword && (
          <TextField
            name={password.key}
            label={password.label}
            required
            fullWidth
            variant="filled"
          />
        )}
      </>
    );
  };

  return (
    <ManagementTable
      ref={tableRef}
      name="User"
      attrs={[email, role, rfidToken]}
      getData={userService.list}
      creationProps={{
        attrs: [email, password],
        handleSubmit: userService.create,
      }}
      updateProps={{
        attrs: [email],
        handleSubmit: userService.update,
      }}
      formMixin={<FormMixin />}
      deleteProps={{
        handleSubmit: userService.remove,
      }}
    />
  );
}
