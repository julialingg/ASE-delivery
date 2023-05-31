import React, { useState } from "react";
import { MenuItem, TextField } from "@mui/material";

export default function Select({
  name,
  label,
  defaultValue,
  options,
  ...props
}) {
  const [displayVal, setDisplayVal] = useState(
    defaultValue ? defaultValue : ""
  );
  const handleChange = (event) => {
    const value = event.target.value;
    setDisplayVal(value);
    if (props.onChange) props.onChange(value);
  };

  return (
    <TextField
      select
      label={label}
      name={name}
      value={displayVal}
      onChange={handleChange}
      required
      variant="filled"
      fullWidth
    >
      {options.map(({ value, label }) => (
        <MenuItem key={value} value={value}>
          {label}
        </MenuItem>
      ))}
    </TextField>
  );
}
