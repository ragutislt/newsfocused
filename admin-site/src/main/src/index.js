import { createTheme, ThemeProvider } from '@mui/material/styles';
import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import AdminApp from './js/AdminApp';

const theme = createTheme({
  spacing: 4
});
theme.spacing(2);

ReactDOM.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <AdminApp />
    </ThemeProvider>
  </React.StrictMode>,
  document.getElementById('root')
);
