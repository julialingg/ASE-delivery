import React, { useEffect, useState } from "react";
import { Autocomplete as MuiAutocomplete, TextField } from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";

export default function Autocomplete({
  name,
  label,
  labelKey,
  defaultValue,
  getOptions,
  onChange,
}) {
  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const loading = open && options.length === 0;
  const [value, setValue] = useState(defaultValue);

  useEffect(() => {
    let active = true;

    if (!loading) {
      return undefined;
    }

    (async () => {
      const res = await getOptions();
      if (active) {
        setOptions(res);
      }
    })();

    return () => {
      active = false;
    };
  }, [loading]); // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    if (!open) {
      setOptions([]);
    }
  }, [open]);

  useEffect(() => {
    setValue(defaultValue);
  }, [defaultValue]);

  return (
    <MuiAutocomplete
      autoHighlight
      open={open}
      value={value}
      onOpen={() => setOpen(true)}
      onClose={() => setOpen(false)}
      onChange={(event, newValue) => {
        setValue(newValue);
        if (onChange) onChange(newValue);
      }}
      isOptionEqualToValue={(option, value) =>
        option[labelKey] === value[labelKey]
      }
      getOptionLabel={(option) => (option[labelKey] ? option[labelKey] : "")}
      options={options}
      loading={loading}
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          name={name}
          required
          variant="filled"
          fullWidth
          type="search"
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <React.Fragment>
                {loading ? (
                  <CircularProgress color="inherit" size={20} />
                ) : null}
                {params.InputProps.endAdornment}
              </React.Fragment>
            ),
            onKeyDown: (e) => {
              if (e.key === "Enter") {
                e.preventDefault();
              }
            },
          }}
          sx={[
            {
              "& input::-webkit-search-decoration, & input::-webkit-search-cancel-button,& input::-webkit-search-results-button,& input::-webkit-search-results-decoration":
                {
                  display: "none",
                },
            },
            {
              "& input::-ms-clear,  & input::-ms-reveal": {
                display: "none",
                width: 0,
                height: 0,
              },
            },
          ]}
        />
      )}
    />
  );
}
