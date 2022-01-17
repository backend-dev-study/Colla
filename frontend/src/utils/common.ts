interface Object {
    [key: string]: string | File | null;
}

export const createFormData = (data: Object) => {
    const formData = new FormData();
    Object.keys(data).forEach((key) => formData.append(key, data[key]!));
    return formData;
};
