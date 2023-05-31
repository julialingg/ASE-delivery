import React, { useRef } from "react";
import { Box } from "@mui/material";

export default function Form({ children, sx, ...props }) {
  const form = useRef(null);

  const onSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(form.current);
    let objectData = {};
    for (const [key, value] of formData.entries()) {
      objectData[key] = value;
    }
    props.onSubmit(objectData);
  };

  return (
    <Box
      ref={form}
      component="form"
      onSubmit={onSubmit}
      sx={sx}
    >
      {children}
    </Box>
  );
}
