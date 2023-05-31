import React, {
  forwardRef,
  useImperativeHandle,
  useEffect,
  useState,
} from "react";
import {
  Button,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  // TablePagination,
  TableRow,
  Toolbar,
  Typography,
} from "@mui/material";
import {
  AddCircleOutlineTwoTone,
  AppRegistrationTwoTone,
  DeleteForever,
} from "@mui/icons-material";
import FormDialog from "./dialogs/FormDialog";
import ConfirmationDialog from "./dialogs/ConfirmationDialog";
import Snackbar from "./Snackbar";

const ManagementTable = forwardRef(
  (
    {
      name,
      attrs,
      getData,
      creationProps,
      updateProps,
      formMixin,
      deleteProps,
      selectableRow,
      ...props
    },
    ref
  ) => {
    const [rows, setRows] = useState([]);
    const [selectedRow, setSelectedRow] = useState({
      data: {},
      index: -1,
    });
    const [showConfirmationDialog, setShowConfirmationDialog] = useState(false);
    const [formDialog, setFormDialog] = useState({ open: false });
    const [message, setMessage] = useState({});

    const closeFormDialog = () => setFormDialog({ open: false });

    const closeComfirmationDialog = () => setShowConfirmationDialog(false);

    const getRows = async () => {
      try {
        const res = await getData();
        setRows(res);
      } catch (err) {
        console.log(err);
      }
    };

    useEffect(() => {
      getRows();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    const onRowSelect = (row) => {
      if (selectableRow) props.onRowSelect(row);
    };

    const setSuccessMessage = (action) => {
      let content =
        action === "CREATE"
          ? "Database record succssefully created"
          : action === "UPDATE"
          ? "Database record successfully updated"
          : action === "DELETE"
          ? "Database record successfully deleted"
          : "";
      setMessage({ content });
    };
    const setErrorMessage = (content) => {
      setMessage({ severity: "error", content });
    };

    useImperativeHandle(ref, () => ({
      selectedRow,
    }));

    const handleCreate = async (data) => {
      try {
        const res = await creationProps.handleSubmit(data);
        const newRows = [...rows, res];
        setRows(newRows);
        setSuccessMessage("CREATE");
        closeFormDialog();
      } catch (err) {
        setFormDialog({ ...formDialog, errorMessage: err });
      }
    };

    const handleUpdate = async (newValues) => {
      const { data, index } = selectedRow;
      try {
        let res = await updateProps.handleSubmit(
          Object.assign({ ...data }, newValues)
        );
        const _rows = [...rows];
        _rows[index] = res;
        setRows(_rows);
        setSuccessMessage("UPDATE");
        closeFormDialog();
      } catch (err) {
        setFormDialog({ ...formDialog, errorMessage: err });
      }
    };

    const handleDelete = async () => {
      const { data, index } = selectedRow;
      try {
        await deleteProps.handleSubmit(data.id);
        const _rows = [...rows];
        _rows.splice(index, 1);
        setRows(_rows);
        setSuccessMessage("DELETE");
        closeComfirmationDialog();
      } catch (err) {
        setErrorMessage(err);
      }
    };
    return (
      <React.Fragment>
        <Toolbar
          variant="dense"
          sx={{ display: "flex", justifyContent: "flex-end" }}
        >
          <Button
            variant="contained"
            startIcon={<AddCircleOutlineTwoTone />}
            onClick={() => {
              setSelectedRow({ data: {}, index: -1 });
              setFormDialog({
                open: true,
                title: `Create a ${name}`,
                attrs: [...creationProps.attrs],
                defaultValues: {},
              });
            }}
          >
            {`Create A ${name}`}
          </Button>
        </Toolbar>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                {attrs.map(({ key, label }) => (
                  <TableCell key={key}>{label}</TableCell>
                ))}
                <TableCell>Update</TableCell>
                <TableCell>Delete</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row, index) => (
                <TableRow
                  key={row.id}
                  sx={{
                    "& td": { py: 1 },
                    cursor: selectableRow ? "pointer" : "default",
                  }}
                  onClick={() => onRowSelect(row)}
                >
                  {attrs.map(({ key }) => (
                    <TableCell key={key + index}>{row[key]}</TableCell>
                  ))}
                  <TableCell>
                    <IconButton
                      color="primary"
                      onClick={(e) => {
                        e.stopPropagation();
                        setSelectedRow({ data: row, index });
                        setFormDialog({
                          open: true,
                          title: `Update a ${name}`,
                          attrs: [...updateProps.attrs],
                          onSubmit: (data) =>
                            handleUpdate(
                              Object.assign({ ...row }, data),
                              index
                            ),
                        });
                      }}
                    >
                      <AppRegistrationTwoTone />
                    </IconButton>
                  </TableCell>
                  <TableCell>
                    <IconButton
                      color="primary"
                      onClick={(e) => {
                        e.stopPropagation();
                        setSelectedRow({ data: row, index });
                        setShowConfirmationDialog(true);
                      }}
                    >
                      <DeleteForever />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        {formDialog.open && (
          <FormDialog
            {...formDialog}
            defaultValues={selectedRow.data}
            onClose={closeFormDialog}
            onSubmit={selectedRow.index > -1 ? handleUpdate : handleCreate}
          >
            {formMixin && formMixin}
          </FormDialog>
        )}
        {showConfirmationDialog && (
          <ConfirmationDialog
            title="Are you sure to delete this record?"
            open={showConfirmationDialog}
            onClose={closeComfirmationDialog}
            onConfirm={handleDelete}
          >
            {attrs.map(({ label, key }) => (
              <Typography key={key} variant="body1" component="p">
                {label + ": " + selectedRow.data[key]}
              </Typography>
            ))}
          </ConfirmationDialog>
        )}

        {message.content && (
          <Snackbar message={message} handleClose={() => setMessage({})} />
        )}
      </React.Fragment>
    );
  }
);

export default ManagementTable;
