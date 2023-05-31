import React from "react";
import {
  Alert,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Typography,
} from "@mui/material";
import Select from "../form/Select";
import Form from "../form/Form";

export default function FormDialog({
  open,
  title,
  attrs,
  defaultValues,
  errorMessage,
  children,
  onClose,
  onSubmit,
}) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth>
      <Form onSubmit={onSubmit}>
        <DialogTitle>{title}</DialogTitle>
        <DialogContent sx={{ "& > :not(style)": { my: 2 } }}>
          {attrs.map(({ type, key, readOnly, label, ...props }) =>
            readOnly ? (
              <Typography key={key} variant="body1" component="p">
                {`${label}: ${defaultValues[key]}`}
              </Typography>
            ) : type === "select" ? (
              <Select
                key={key}
                name={key}
                label={label}
                defaultValue={defaultValues[key] ? defaultValues[key] : ""}
                {...props}
              />
            ) : (
              <TextField
                required
                key={key}
                name={key}
                type={type}
                label={label}
                defaultValue={defaultValues ? defaultValues[key] : ""}
                fullWidth
                variant="filled"
              />
            )
          )}
          {children}
          {errorMessage && (
            <Alert severity="error" style={{ marginBottom: 0 }}>
              {errorMessage}
            </Alert>
          )}
        </DialogContent>
        <DialogActions sx={{ px: 3, pt: 0, pb: 2 }}>
          <Button onClick={onClose}>Cancel</Button>
          <Button type="submit" variant="contained">
            Submit
          </Button>
        </DialogActions>
      </Form>
    </Dialog>
  );
}
