import { createTheme, ThemeProvider } from '@mui/material/styles';
import React from 'react';
import ReactDOM from 'react-dom';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './css/index.css';
import AdminApp from './js/AdminApp';
import { UserPage } from './js/users/UserPage';

const theme = createTheme({
  spacing: 4
});
theme.spacing(2);

const router = createBrowserRouter([
  {
    path: "/",
    element: <AdminApp/>,
  },
  {
    path: "/users/:email",
    element: <UserPage/>,
  },
]);

ReactDOM.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <RouterProvider router={router} />
    </ThemeProvider>
  </React.StrictMode >,
  document.getElementById('root')
);
